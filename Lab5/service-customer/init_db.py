from models import db, Customer

def init_database(customer_repository):
    """Initialize database with sample data using the repository pattern"""
    # Check if we already have customers
    if not customer_repository.find_all():
        # Add sample customers
        customers = [
            {
                'name': 'John Doe',
                'email': 'john.doe@example.com',
                'phone': '555-1234'
            },
            {
                'name': 'Jane Smith',
                'email': 'jane.smith@example.com',
                'phone': '555-5678'
            }
        ]
        
        for customer_data in customers:
            customer_repository.create(
                name=customer_data['name'],
                email=customer_data['email'],
                phone=customer_data['phone']
            )
            
        print("Sample data initialized")
    else:
        print("Database already contains data")

if __name__ == '__main__':
    init_database() 