
//in - attribute
//out - varying
attribute vec3 position;

attribute vec2 textureCoords;
varying vec2 pass_textureCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){

	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
	pass_textureCoords = textureCoords;
}