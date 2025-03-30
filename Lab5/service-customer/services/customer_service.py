from models import Customer

class CustomerService:
    def __init__(self, customer_repository):
        self.customer_repository = customer_repository
    
    def get_customer(self, customer_id):
        """Get customer by ID"""
        customer = self.customer_repository.find_by_id(customer_id)
        if customer:
            return customer.to_dict(), False
        
        return None, False
    
    def create_customer(self, name, email, phone=None):
        """Create a new customer"""
        # Check if email already exists
        existing_customer = self.customer_repository.find_by_email(email)
        if existing_customer:
            return None, "Email already registered"
        
        # Create new customer
        customer = self.customer_repository.create(name, email, phone)
        
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
        
        return customer.to_dict(), None
    
    def delete_customer(self, customer_id):
        """Delete a customer"""
        customer = self.customer_repository.find_by_id(customer_id)
        if not customer:
            return False, "Customer not found"
        
        # Delete from database
        self.customer_repository.delete(customer)
        
        return True, None 