import os
import re

def fix_templates():
    template_dir = 'templates'
    for root, _, files in os.walk(template_dir):
        for file in files:
            if file.endswith('.html'):
                filepath = os.path.join(root, file)
                with open(filepath, 'r') as f:
                    content = f.read()

                # Replace {{ csrf_token }} with {{ csrf_token() }}
                content = content.replace("{{ csrf_token }}", "{{ csrf_token() }}")

                with open(filepath, 'w') as f:
                    f.write(content)

if __name__ == '__main__':
    fix_templates()
