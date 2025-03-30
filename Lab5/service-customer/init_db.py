from models import db, Customer

def init_database(app, customer_repository):
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
            # Wrong pattern, but hotfix for the example
            for customer in customers:
                customer_repository.save(customer)
                
            print("Sample data initialized")
        else:
            print("Database already contains data")

if __name__ == '__main__':
    init_database() 