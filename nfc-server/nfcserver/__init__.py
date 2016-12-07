from flask import Flask


def create_app():
    app = Flask(__name__)

    from nfcserver.db import DBManager
    db_url = 'mysql+pymysql://root:rlaqhdns@localhost/nfc?charset=utf8'
    DBManager.init(db_url, True)
    DBManager.init_db()

    return app
