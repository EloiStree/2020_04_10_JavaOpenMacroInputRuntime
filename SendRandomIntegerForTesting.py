import socket
import struct
import random
import time

def main():
	server_address = ('127.0.0.1', 2502)
	sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

	try:
		while True:
			random_int = random.randint(1060, 1080)
			little_endian_data = struct.pack('<I', random_int)
			sock.sendto(little_endian_data, server_address)
			print(f'Sent: {random_int} as {little_endian_data}')
			time.sleep(1)
			random_int +=1000
			little_endian_data = struct.pack('<I', random_int)
			sock.sendto(little_endian_data, server_address)
			print(f'Sent: {random_int} as {little_endian_data}')
			time.sleep(1)
	except KeyboardInterrupt:
		print("Stopped by user")
	finally:
		sock.close()

if __name__ == "__main__":
	main()