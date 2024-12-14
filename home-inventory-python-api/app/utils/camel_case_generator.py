def camel_case_generator(field: str) -> str:
    """Generate a camelCase alias from a snake_case string."""
    return "".join(word.capitalize() if i else word for i, word in enumerate(field.split("_")))
