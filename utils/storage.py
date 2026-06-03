import os

class LocalStorage:
    def __init__(self, base_dir):
        self.base_dir = base_dir
        if not os.path.exists(self.base_dir):
            os.makedirs(self.base_dir)

    def save(self, file_storage, filename):
        file_path = os.path.join(self.base_dir, filename)
        file_storage.save(file_path)
        return file_path

    def get_url(self, filename):
        return f"/uploads/{filename}"

    def delete(self, filename):
        file_path = os.path.join(self.base_dir, filename)
        if os.path.exists(file_path):
            os.remove(file_path)
