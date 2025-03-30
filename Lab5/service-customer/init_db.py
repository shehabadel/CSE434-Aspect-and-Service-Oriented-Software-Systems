from app import app, db, customer_repository
from models import Customer  # This import will now come from models/__init__.py

def init_database():
    with app.app_context():
        # Create tables
        db.create_all()
        
        # Check if we already have customers
        if Customer.query.count() == 0:
            # Add sample customers
            customers = [
                Customer(
                    name='John Doe',
                    email='john.doe@example.com',
                    phone='555-1234'
                ),
                Customer(
                    name='Jane Smith',
                    email='jane.smith@example.com',
                    phone='555-5678'
                )
            ]
            
            for customer in customers:
                customer_repository.save(customer)
                
            print("Sample data initialized")
        else:
            print("Database already contains data")

if __name__ == '__main__':
    init_database() 