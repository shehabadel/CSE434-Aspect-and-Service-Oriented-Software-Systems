from models import Customer

class CustomerService:
    def __init__(self, customer_repository, redis_utils):
        self.customer_repository = customer_repository
        self.redis_utils = redis_utils
    
    def get_customer(self, customer_id):
        """Get customer by ID with caching"""
        # Try to get from cache first
        cached_customer = self.redis_utils.get_customer_from_cache(customer_id)
        if cached_customer:
            return cached_customer, True  # Second param indicates "from cache"
        
        # If not in cache, get from database
        customer = self.customer_repository.find_by_id(customer_id)
        if customer:
            # Cache the customer data
            self.redis_utils.set_customer_to_cache(customer)
            return customer.to_dict(), False  # Not from cache
        
        return None, False
    
    def create_customer(self, name, email, phone=None):
        """Create a new customer"""
        # Check if email already exists
        existing_customer = self.customer_repository.find_by_email(email)
        if existing_customer:
            return None, "Email already registered"
        
        # Create new customer
        customer = self.customer_repository.create(name, email, phone)
        
        # Cache the new customer
        self.redis_utils.set_customer_to_cache(customer)
        
        return customer.to_dict(), None
    
    def update_customer(self, customer_id, data):
        """Update a customer"""
        customer = self.customer_repository.find_by_id(customer_id)
        if not customer:
            return None, "Customer not found"
        
        # Update email if provided and check for uniqueness
        if 'email' in data:
            existing = self.customer_repository.find_by_email(data['email'])
            if existing and existing.id != customer_id:
                return None, "Email already in use"
            customer.email = data['email']
        
        # Update other fields if provided
        if 'name' in data:
            customer.name = data['name']
        if 'phone' in data:
            customer.phone = data['phone']
        
        # Save changes
        self.customer_repository.save(customer)
        
        # Update cache
        self.redis_utils.invalidate_customer_cache(customer_id)
        self.redis_utils.set_customer_to_cache(customer)
        
        return customer.to_dict(), None
    
    def delete_customer(self, customer_id):
        """Delete a customer"""
        customer = self.customer_repository.find_by_id(customer_id)
        if not customer:
            return False, "Customer not found"
        
        # Delete from database
        self.customer_repository.delete(customer)
        
        # Invalidate cache
        self.redis_utils.invalidate_customer_cache(customer_id)
        
        return True, None 