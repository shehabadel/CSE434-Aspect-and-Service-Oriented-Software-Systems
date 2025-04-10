package com.example.Lab6.Room;

import com.example.Lab6.Annotations.DistributedLock;
import com.example.Lab6.Redis.RedisClient;
import com.example.Lab6.Room.dto.CreateRoomDTO;
import com.example.Lab6.Room.dto.UpdateRoomDTO;
import com.example.Lab6.Room.Room;
import com.example.Lab6.Room.RoomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomService.class); // Add logger
    @Autowired
    private RoomRepository roomRepository;
    @Autowired  
    private RedisClient redisClient; // Inject RedisClient
    @Autowired
    private ObjectMapper objectMapper; // Inject ObjectMapper
    
    private static final String ALL_ROOMS_CACHE_KEY = "rooms:all"; // Define cache key
    private static final Duration CACHE_TTL = Duration.ofMinutes(10); // Define cache TTL (e.g., 10 minutes)
    private static final String ROOM_LOCK_PREFIX = "room";

    // Get all rooms with caching
    public List<Room> getAllRooms() {
        // 1. Try fetching from cache
        try {
            String cachedRoomsJson = redisClient.get(ALL_ROOMS_CACHE_KEY);
            if (cachedRoomsJson != null) {
                log.info("Cache hit for key: {}", ALL_ROOMS_CACHE_KEY);
                // Deserialize JSON string back to List<Room>
                List<Room> rooms = objectMapper.readValue(cachedRoomsJson, new TypeReference<List<Room>>() {});
                return rooms;
            }
        } catch (JsonProcessingException e) {
            log.error("Error deserializing cached rooms from Redis for key {}: {}", ALL_ROOMS_CACHE_KEY, e.getMessage());
            // Proceed to fetch from DB if deserialization fails
        } catch (Exception e) {
            log.error("Error accessing Redis for key {}: {}", ALL_ROOMS_CACHE_KEY, e.getMessage());
            // Proceed to fetch from DB if Redis access fails
        }
        // 2. If cache miss or error, fetch from database
        log.info("Cache miss for key: {}. Fetching from database.", ALL_ROOMS_CACHE_KEY);
        List<Room> rooms = roomRepository.findAll();
        // 3. Store the result in cache
        if (rooms != null && !rooms.isEmpty()) {
            try {
                String roomsJson = objectMapper.writeValueAsString(rooms);
                redisClient.set(ALL_ROOMS_CACHE_KEY, roomsJson, CACHE_TTL); // Use set with TTL
                log.info("Stored {} rooms in cache for key: {}", rooms.size(), ALL_ROOMS_CACHE_KEY);
            } catch (JsonProcessingException e) {
                log.error("Error serializing rooms for caching for key {}: {}", ALL_ROOMS_CACHE_KEY, e.getMessage());
            } catch (Exception e) {
                log.error("Error storing data in Redis for key {}: {}", ALL_ROOMS_CACHE_KEY, e.getMessage());
            }
        }
        return rooms;
    }
    // Get room by ID
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    // Get room by Room Number
    public Optional<Room> getRoomByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }


    // Create a new room
    @Transactional
    public Room createRoom(CreateRoomDTO roomDTO) {
        // Optional: Check if room number already exists
        if (roomRepository.findByRoomNumber(roomDTO.getRoomNumber()).isPresent()) {
            throw new IllegalArgumentException("Room number " + roomDTO.getRoomNumber() + " already exists.");
        }

        Room room = new Room(
                roomDTO.getRoomNumber(),
                roomDTO.getType(),
                roomDTO.getPrice()
        );
        // isReserved defaults to false in the constructor
        return roomRepository.save(room);
    }

    // Update an existing room
    @Transactional
    @DistributedLock(keyPrefix = ROOM_LOCK_PREFIX, keyIdentifierExpression = "#id", leaseTime = 120, timeUnit = TimeUnit.SECONDS)
    public Room updateRoom(Long id, UpdateRoomDTO roomDTO) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));

        // Optional: Check if the new room number (if changed) already exists for another room
        if (roomDTO.getRoomNumber() != null && !roomDTO.getRoomNumber().equals(existingRoom.getRoomNumber())) {
            if (roomRepository.findByRoomNumber(roomDTO.getRoomNumber()).isPresent()) {
                throw new IllegalArgumentException("Room number " + roomDTO.getRoomNumber() + " already exists.");
            }
        }

        // Copy non-null properties from DTO to entity
        BeanUtils.copyProperties(roomDTO, existingRoom, getNullPropertyNames(roomDTO));

        // --- Simulate long-running operation ---
        log.info("Room update for id {}: Starting 3-minute delay...", id);
        try {
            TimeUnit.MINUTES.sleep(3); // Wait for 3 minutes
        } catch (InterruptedException e) {
            log.warn("Room update for id {}: Sleep interrupted!", id);
            // Restore the interrupted status
            Thread.currentThread().interrupt();
            // Optionally rethrow as a runtime exception if interruption should stop processing
            // throw new RuntimeException("Update process interrupted", e);
        }
        log.info("Room update for id {}: Finished 3-minute delay. Saving...", id);
        // --- End simulation ---

        Room updatedRoom = roomRepository.save(existingRoom); // Save after the delay

        return updatedRoom; // Return after saving
    }

    // Delete a room
    @Transactional
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
        roomRepository.delete(room);
    }

    // Helper method to find null properties in the DTO for partial updates
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
} 