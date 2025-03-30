from flask import Flask, jsonify
from flask_migrate import Migrate
import os
from models import db
from config import Config
from repositories.customer_repository import CustomerRepository
from services.customer_service import CustomerService
from controllers.customer_controller import CustomerController, customer_bp
from init_db import init_database

app = Flask(__name__)
app.config.from_object(Config)

# Initialize extensions
db.init_app(app)
migrate = Migrate(app, db)

# Initialize components
customer_repository = CustomerRepository()
customer_service = CustomerService(customer_repository)
customer_controller = CustomerController(customer_service)

# Register blueprints
app.register_blueprint(customer_bp)

# Health check route
@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy"}), 200

if __name__ == '__main__':
    with app.app_context():
        db.create_all()
        init_database(customer_repository)
    app.run(host='0.0.0.0', port=int(os.getenv('PORT', 5000)), debug=True) 