# Use an official Python runtime as the base image
FROM python:3.11-alpine

# Set the working directory in the container
WORKDIR /app

# Copy only the requirements file to leverage Docker's caching
COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code
COPY . .

# Expose the application's port
EXPOSE 8085

# Declare build arguments (used during build)
ARG DATABASE_URL
ARG EMAIL_ADDRESS
ARG EMAIL_PASSWORD

# Set environment variables (available at runtime)
ENV DATABASE_URL=${DATABASE_URL}
ENV EMAIL_ADDRESS=${EMAIL_ADDRESS}
ENV EMAIL_PASSWORD=${EMAIL_PASSWORD}

# Command to run the application directly
CMD ["uvicorn", "app.app:app", "--host", "0.0.0.0", "--port", "8085", "--reload"]