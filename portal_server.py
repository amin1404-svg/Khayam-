import http.server
import socketserver
import os

PORT = 3000
DIRECTORY = os.path.join(os.path.dirname(os.path.abspath(__file__)), "download_portal")

class CustomHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def guess_type(self, path):
        if path.endswith(".apk"):
            return "application/vnd.android.package-archive"
        return super().guess_type(path)

    def end_headers(self):
        self.send_header('Access-Control-Allow-Origin', '*')
        super().end_headers()

if __name__ == '__main__':
    socketserver.TCPServer.allow_reuse_address = True
    with socketserver.TCPServer(("", PORT), CustomHandler) as httpd:
        print(f"Serving download portal at port {PORT} from {DIRECTORY}")
        httpd.serve_forever()
