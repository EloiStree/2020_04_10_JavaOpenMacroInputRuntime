import socket
import time
import datetime
#Type 'python' in the command line to install python.
def sendmessage(msg):
  print("message: %s" % msg)
  sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
  sock.sendto(msg.encode(), (UDP_IP, UDP_PORT))
def trycmd(msg,description):
  print("")
  print("Try to do: %s" % description)
  print("Sent: %s" % msg)
  print("")
  sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
  sock.sendto(msg.encode(), (UDP_IP, UDP_PORT))
  time.sleep(1)
def linereturn():
  sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
  sock.sendto("sc:Enter↕".encode(), (UDP_IP, UDP_PORT))
  time.sleep(0.1)


UDP_IP = "127.0.0.1"
UDP_PORT = 2501

"""
#START
print("UDP target IP: %s" % UDP_IP)
print("UDP target port: %s" % UDP_PORT)
print("You have 3 seconds when enter is press to go in a text editor")
input("Press Enter to continue...")

# Count Down
print("3...")
time.sleep(1)
print("2...")
time.sleep(1)
print("1...")
time.sleep(1)

# Hello World
trycmd("sc: [[Hello World]]"
  ,"Write 'Hello World'")
linereturn()
# Use t:hh-mm-ss-ms: 
now=datetime.datetime.now()
t=('t:%02d-%02d-%02d-0:'%(now.hour,now.minute,now.second+1))
trycmd(t+"sc: [[It is time]] Enter↕"
  ,"")
sendmessage("tms:100:sc:shift+t")
sendmessage("tms:300:ks:i")
sendmessage("tms:500:ks:m")
sendmessage("tms:600:ks:e")
sendmessage("tms:800:ks:vk_enter")
sendmessage("tms:900:sc:[[!]] 100> [[!]] 100> [[!]]")
sendmessage("tms:1400:ks:vk_enter")
sendmessage("tms:1600:sc:( [[! ]] 100> ) x 10 ")
time.sleep(4)
linereturn()
# Basic
trycmd("ks:VK_ENTER","Stroke Enter key")
trycmd("ks:VK_A","Stroke 'a'")
trycmd("kp:VK_B","Press 'b'")
trycmd("kr:VK_B","Release 'b'")
trycmd("ks:a","Stroke A alias of VK_A if it exists")
trycmd("ksc:ks:d裂ks:e","Group two commands splite by 裂")
linereturn()
trycmd("past:I love the patato"
  ,"Past from clipboard a text")
trycmd(t+"sc: ctrl+a"
  ,"Select all the text")
trycmd("clipboard:copy","Use clipboard to copy")
linereturn()
trycmd("clipboard:past","Use clipboard to past")
linereturn()
trycmd("clipboard:past","Use clipboard to past")
linereturn()
trycmd(t+"sc: ctrl+a"
  ,"Select all the text")
linereturn()
trycmd("clipboard:copypast","Use clipboard to copy then past")
linereturn()
# https://unicode-table.com/en/1F9F0/
trycmd("unicode:129520","Write a unicode with int number")
trycmd("unicode:U+1F9F0","Write a unicode with hexa decimal")
linereturn()
"""


time.sleep(3)
#trycmd("sc: Enter↕ ( ( [[.]] Enter↕  )x5  [[---]] Enter↕  )x20  ","Write a unicode with int number")
#trycmd("ksc:tms:0:wh:-3裂tms:1000:wh:-3裂tms:2000:wh:20","");

#trycmd("sc: window↕ 2000> [[paint]] 100> space↕ 2000> Enter↕","Launch paint")
#trycmd("sc: window+up ","Full screen")

#trycmd("mm:0.6%:0.3%","")
#trycmd("ms:l","")
"""
trycmd("mm:0.1%:0.7%","L T")
trycmd("ms:l","")
trycmd("mm:0.9%:0.7%","R T")
trycmd("ms:l","")
trycmd("mm:0.1%:0.2%","L D")
trycmd("ms:l","")
trycmd("mm:0.9%:0.2%","R D")
trycmd("ms:l","")
"""

##trycmd("sc:alt↕ h↕ b↕","Brush")
trycmd("mm:0.173%:0.935%","L T")
trycmd("ms:l","")
trycmd("mm:0.5%:0.5%","L T")
trycmd("ms:l","")

"""
trycmd("mm:0.1%:0.7%","L T")
trycmd("mp:l","")
trycmd("mm:0.9%:0.7%","R T")
trycmd("mr:l","")

#trycmd("mm:0.9%:0.98%","R T")
#trycmd("ms:r","")

trycmd("mm:0.5%:0.5%","R T")
trycmd("ms:l","")
"""

"""
trycmd("mm:0.5%:0.5%","R T")
trycmd("mp:l","")
trycmd("ma:60px:0px","")
trycmd("ma:0px:-60px","")
trycmd("ma:-60px:0px","")
trycmd("ma:0px:60px","")

trycmd("ma:-0.05p:0.0p","")
trycmd("ma:0.0p:0.05p","")
trycmd("ma:0.05p:0.0p","")
trycmd("ma:0.0p:-0.05p","")
trycmd("mr:l","")
"""



#"""
trycmd("sc:🐁A↓0.3←0.3px 10> 🐁LeftClick ","R T")
trycmd("sc:🐁A→100↑100 10> 🐁LeftClick ","R T")


trycmd("sc:( 🐁→10 10> 🐁LeftClick 10>)x20  ","R T")
trycmd("sc:( 🐁↑10 10> 🐁LeftClick 10>)x20  ","R T")
trycmd("sc:( 🐁←10 10> 🐁LeftClick 10>)x10  ","R T")
trycmd("sc:( 🐁↓10 10> 🐁LeftClick 10>)x5  ","R T")
trycmd("sc:( 🐁→0.01% 10> 🐁l 10>)x20  ","R T")
trycmd("sc:( 🐁↑0.01% 10> 🐁l 10>)x20  ","R T")
trycmd("sc:( 🐁←0.01% 10> 🐁l 10>)x10  ","R T")
trycmd("sc:( 🐁↓0.01% 10> 🐁l 10>)x5  ","R T")
trycmd("sc:( 🐁↑0.005→20px 10> 🐁l 10>)x5  ","R T")
trycmd("sc:( 🐁↑20px→0.005% 10> 🐁l 10>)x5  ","R T")
trycmd("sc:( leftclick↓ 10> 🐁→200px leftclick↑ 10>)x1  ","R T")
trycmd("sc:( leftclick↓ 10> 🐁↑200px leftclick↑ 10>)x1  ","R T")
trycmd("sc:( leftclick↓ 10> 🐁←200px leftclick↑ 10>)x1  ","R T")
trycmd("sc:( leftclick↓ 10> 🐁↓200px leftclick↑ 10>)x1  ","R T")
#"""


trycmd("img2clip:https://i.imgflip.com/4k39g9.jpg","")
trycmd("sc:ctrl+v 1000> ctrl+v  ","")
trycmd("sc: ( ctrl+pagedown )x5","")


"""

trycmd("ks:VK_A","Stroke 'a'")
trycmd("kp:VK_B","Press 'b'")
trycmd("kr:VK_B","Release 'b'")

for x in range(40):
  sendmessage("wh:-1")
  time.sleep(0.1)
for x in range(40):
  sendmessage("wh:1")
  time.sleep(0.1)
  
 """


  





  




