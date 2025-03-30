import json
from flask import current_app

class RedisUtils:
    def __init__(self, redis_client):
        self.redis_client = redis_client
    
    def get_customer_from_cache(self, customer_id):
        """Get customer data from Redis cache"""
        cache_key = f"customer:{customer_id}"
        cached_data = self.redis_client.get(cache_key)
        if cached_data:
            return json.loads(cached_data)
        return None
    
    def set_customer_to_cache(self, customer):
        """Set customer data to Redis cache"""
        cache_key = f"customer:{customer.id}"
        expiration = current_app.config['CACHE_EXPIRATION']
        self.redis_client.setex(
            cache_key,
            expiration,
            json.dumps(customer.to_dict())
        )
    
    def invalidate_customer_cache(self, customer_id):
        """Remove customer data from cache"""
        cache_key = f"customer:{customer_id}"
        self.redis_client.delete(cache_key) 