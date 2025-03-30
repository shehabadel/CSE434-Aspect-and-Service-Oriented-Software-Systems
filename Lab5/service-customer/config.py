import os
from dotenv import load_dotenv

load_dotenv()

class Config:
    
    # Database
    SQLALCHEMY_DATABASE_URI = os.getenv(
        'DATABASE_URL', 
        'postgresql://postgres:postgres@service-customer-pg:5432/service-customer'
    )
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    
    # Redis
    REDIS_URL = os.getenv('REDIS_URL', 'redis://service-customer-redis:6379/0')
    CACHE_EXPIRATION = 3600  # Cache expiration in seconds (1 hour) 