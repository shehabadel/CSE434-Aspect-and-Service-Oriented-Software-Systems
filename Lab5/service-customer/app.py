from flask import Flask, jsonify
from flask_migrate import Migrate
import redis
import os
from models import db
from config import Config
from repositories.customer_repository import CustomerRepository
from services.customer_service import CustomerService
from controllers.customer_controller import CustomerController, customer_bp
from utils.redis_utils import RedisUtils

app = Flask(__name__)
app.config.from_object(Config)

# Initialize extensions
db.init_app(app)
migrate = Migrate(app, db)

# Redis connection
redis_client = redis.from_url(app.config['REDIS_URL'])

# Initialize components
redis_utils = RedisUtils(redis_client)
customer_repository = CustomerRepository()
customer_service = CustomerService(customer_repository, redis_utils)
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
    app.run(host='0.0.0.0', port=int(os.getenv('PORT', 5000)), debug=True) 