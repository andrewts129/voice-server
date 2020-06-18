# voice-server

[![](https://github.com/andrewts129/voice-server/workflows/Build%20%26%20Deploy/badge.svg)](https://github.com/andrewts129/voice-server/actions?query=workflow%3A%22Build+%26+Deploy%22)

Small HTTP service that provides an endpoint for converting text to an .mp3 file using [Festival](http://www.cstr.ed.ac.uk/projects/festival/). Built with [http4s](https://http4s.org/).  
  
## Usage  
To download an .mp3 file that says "My hovercraft is full of eels":  
```GET /my%20hovercraft%20is%20full%20of%20eels```
