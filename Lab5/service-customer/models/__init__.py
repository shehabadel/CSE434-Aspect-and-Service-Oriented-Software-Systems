from flask_sqlalchemy import SQLAlchemy

# Create the SQLAlchemy instance
db = SQLAlchemy()

# Import all models to make them available when importing from models package
from .customer import Customer 