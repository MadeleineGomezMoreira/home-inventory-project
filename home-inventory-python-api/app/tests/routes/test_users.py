from fastapi.testclient import TestClient
from app.app import app

client = TestClient(app)


def test_read_users():
    response = client.get("/users/")

    # Assert the response status code is 200
    assert response.status_code == 200

    # Parse the response to JSON
    users = response.json()

    # Ensure the response is a list
    assert isinstance(users, list)

    # Check if there is at least one user
    assert len(users) > 0

    # Verify the structure of each user object
    for user in users:
        assert "id" in user
        assert "username" in user
        assert "email" in user
        # Check that the values are of the expected type
        assert isinstance(user["id"], int)
        assert isinstance(user["username"], str)
        assert isinstance(user["email"], str)


def test_read_user_by_id():
    user_id = 1  # Existing user_id
    response = client.get(f"/users/{user_id}")

    # Assert the response status code is 200
    assert response.status_code == 200

    # Parse the response to JSON
    user = response.json()

    # Verify the structure of the user object
    assert "id" in user
    assert "username" in user
    assert "email" in user

    # Check that the ID matches
    assert user["id"] == user_id


def test_read_user_by_wrong_id():
    user_id = -1  # Non-existant user_id
    response = client.get(f"/users/{user_id}")

    # Assert the response status code is 404
    assert response.status_code == 404


def test_read_user_by_invalid_id():
    user_id = "b"  # Invalid user_id format
    response = client.get(f"/users/{user_id}")

    # Assert the response status code is 422
    assert response.status_code == 422
