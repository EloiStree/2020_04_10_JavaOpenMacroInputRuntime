import socket
UDP_IP = "127.0.0.1"
UDP_PORT = 2501
MESSAGE = "tms:5000:sc: enter↓ enter↑ [[Hello world !!!]] enter↓ enter↑"
print("UDP target IP: %s" % UDP_IP)
print("UDP target port: %s" % UDP_PORT)
print("message: %s" % MESSAGE)
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.sendto(MESSAGE.encode(), (UDP_IP, UDP_PORT))