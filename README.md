## Running on MacOS

* Make sure socat is installed: `brew insall socat`
* Run: `socat -4 TCP-LISTEN:2375,fork UNIX-CONNECT:/var/run/docker.sock`