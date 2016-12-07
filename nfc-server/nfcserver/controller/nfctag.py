import time, nxppy


def getNFCTag():
    print('NFC TAG')
    mifare = nxppy.Mifare()

    # Print card UIDs as they are detected
    while True:
        try:
            uid = mifare.select()
            print(uid)
            return uid
        except nxppy.SelectError:
            # SelectError is raised if no card is in the field.
            # print('nxppy.SelectError')
            pass
        time.sleep(1)
