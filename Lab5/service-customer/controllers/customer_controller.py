from flask import Blueprint, request, jsonify

customer_bp = Blueprint('customer', __name__)

class CustomerController:
    def __init__(self, customer_service):
        self.customer_service = customer_service
        
        # Register routes
        customer_bp.route('/customers', methods=['POST'])(self.create_customer)
        customer_bp.route('/customers', methods=['GET'])(self.get_customers)
        customer_bp.route('/customers/<int:customer_id>', methods=['GET'])(self.get_customer)
        customer_bp.route('/customers/<int:customer_id>', methods=['PUT'])(self.update_customer)
        customer_bp.route('/customers/<int:customer_id>', methods=['DELETE'])(self.delete_customer)
    
    def create_customer(self):
        data = request.get_json()
        
        if not data or not all(key in data for key in ['name', 'email']):
            return jsonify({"error": "Missing required fields (name, email)"}), 400
        
        customer, error = self.customer_service.create_customer(
            data['name'], 
            data['email'], 
            data.get('phone')
        )
        
        if error:
            return jsonify({"error": error}), 409
        
        return jsonify(customer), 201
    
    def get_customer(self, customer_id):
        customer, _ = self.customer_service.get_customer(customer_id)
        
        if not customer:
            return jsonify({"error": "Customer not found"}), 404
        
        return jsonify(customer), 200
    
    def get_customers(self):
        customers, _ = self.customer_service.get_customers()
        
        if not customers:
            return jsonify({"error": "No customers found"}), 404
        
        return jsonify(customers), 200


    def update_customer(self, customer_id):
        data = request.get_json()
        
        customer, error = self.customer_service.update_customer(customer_id, data)
        
        if error:
            if error == "Customer not found":
                return jsonify({"error": error}), 404
            return jsonify({"error": error}), 409
        
        return jsonify(customer), 200
    
    def delete_customer(self, customer_id):
        success, error = self.customer_service.delete_customer(customer_id)
        
        if not success:
            return jsonify({"error": error}), 404
        
        return jsonify({"message": "Customer deleted"}), 200 