from fastapi.testclient import TestClient
from app.app import app
import random as r
from random import randint

client = TestClient(app)


def test_read_homes():
    response = client.get("/homes/")

    # Assert the response status code is 200
    assert response.status_code == 200

    # Parse the response to JSON
    homes = response.json()

    # Ensure the response is a list
    assert isinstance(homes, list)

    # Check if there is at least one home
    assert len(homes) > 0

    # Verify the structure of each home object
    for home in homes:
        assert "id" in home
        assert "home_name" in home
        assert "owned_by" in home
        # Check that the values are of the expected type
        assert isinstance(home["id"], int)
        assert isinstance(home["home_name"], str)
        assert isinstance(home["owned_by"], int)


def test_save_home():
    # Generate a random number
    random_number = r.randint(1000, 9999)

    # Generate a random house name
    random_home_name = "Casa de {}".format(random_number)

    home_data = {"homeName": random_home_name, "ownedBy": 3}

    response = client.post("/homes/", json=home_data)

    # Assert the response status code is 201
    assert response.status_code == 201

    # Validate response's JSON structure
    saved_home = response.json()
    assert "id" in saved_home
    assert saved_home["home_name"] == random_home_name
    assert saved_home["owned_by"] == 3


def test_save_home_invalid_home_name():
    home_data = {"homeName": 3, "ownedBy": 3}

    response = client.post("/homes/", json=home_data)

    # Assert the response status code is 422
    assert response.status_code == 422


def test_save_home_invalid_owner():
    home_data = {"homeName": "Home name", "ownedBy": "word"}

    response = client.post("/homes/", json=home_data)

    # Assert the response status code is 422
    assert response.status_code == 422


def test_save_home_missing_body():
    response = client.post("/homes/")

    # Assert the response status code is 422
    assert response.status_code == 422


def test_save_home_missing_owner():

    # Generate a random number
    random_number = r.randint(1000, 9999)

    # Generate a random house name
    random_home_name = "Casa de {}".format(random_number)

    home_data = {"homeName": random_home_name}

    response = client.post("/homes/", json=home_data)

    # Assert the response status code is 422
    assert response.status_code == 422


def test_save_home_missing_home_name():

    home_data = {"ownedBy": 3}

    response = client.post("/homes/", json=home_data)

    # Assert the response status code is 422
    assert response.status_code == 422
