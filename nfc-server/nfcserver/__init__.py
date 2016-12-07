from flask import Flask


def create_app():
    app = Flask(__name__)

    from nfcserver.db import DBManager
    db_url = 'mysql+pymysql://root:rlaqhdns@localhost/nfc?charset=utf8'
    DBManager.init(db_url, True)
    DBManager.init_db()

    from nfcserver.controller import signup
    from nfcserver.blueprint import nfc
    app.register_blueprint(nfc)

    return app
