# WaveInterference

Start the programm from AppWindow.java. It opens a drawing interface, where point and line wave sources can be drawn manually or randomly generated using the wave generator. 
The wave properties include: 
  - location
  - amplitude
  - wavelength
  - phase
  - type: linear or exponential, featuring a deca constant
 
The wave generator generates N waves randomly alterning these properties in a range, defined by the user.
Drawn wave stups can be saved by choosing a name in the field situated at the top "Insert file name" and then pressing enter + "Safe". It creates a json file with all information.
for leading, type the same name and click on "Load". To reset the canvas (deleting all waves) press "Reset".

From the drawn/generated wave set, 4 options can be selected from the drop-dwon menu atthe top right of the screen:
- Realtime: 
  Draws the waves and updates the frame constantly, simulating propagating waves.
- Image:
  Generates and saves one image of the interference patttern.
- Frame:
  Generates multiple frames of the interference patttern at different propagation statuses. (Seems to be buggy at the moment)
 - Iterations:
 First generates and saves an interference pattern. Then, according to the Itteration setting selected in the popup-menu, selects suitable new point sources,
 by assuming the last image to be a topographical image inducing scattering. With the right settings this most often leads to the most stable hexagonal pattern
 after several ten iteration. A randomly occuring hexagonal pattern is proliferated by the simulated feedback-loop.


Warning: There are a few deprecated methods in the code, which were expected to add additonal functionality, e.g. simulating moving laser spots for the iteration process. These turned out to be erroreous and where abandoned.
