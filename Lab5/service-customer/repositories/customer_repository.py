from models import db, Customer

class CustomerRepository:
    def find_by_id(self, customer_id):
        """Find a customer by ID"""
        return Customer.query.get(customer_id)
    
    def find_by_email(self, email):
        """Find a customer by email"""
        return Customer.query.filter_by(email=email).first()
    
    def save(self, customer):
        """Save a customer to the database"""
        db.session.add(customer)
        db.session.commit()
        return customer
    
    def delete(self, customer):
        """Delete a customer from the database"""
        db.session.delete(customer)
        db.session.commit()
    
    def create(self, name, email, phone=None):
        """Create a new customer"""
        customer = Customer(name=name, email=email, phone=phone)
        return self.save(customer) 