import os
import sys
import django

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'monkey.settings') 	# settings.py가 있는곳
django.setup()

import sqlite3
from main.models import Answer

def save():
    database = "db.sqlite3"
    conn = sqlite3.connect(database)

    answer = Answer(name='kyh', answer='5723')
    answer.save()

    conn.close()

def clear():
    # Connect to (create) database.
    database = "db.sqlite3"
    conn = sqlite3.connect(database)
    cur = conn.cursor()
    cur.execute("DELETE FROM main_answer")
    conn.commit()
    conn.close()

if __name__ == '__main__':
    clear()
    save()