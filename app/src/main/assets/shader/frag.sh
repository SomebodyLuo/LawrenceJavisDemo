
precision mediump float;
uniform sampler2D sTexture_;//纹理内容数据
uniform float uAlpha;
float alphaFinal ;


varying vec2 vTextureCoord_; //接收从顶点着色器过来的参数
varying vec4 vAmbient; //接收从从顶点着色器的环境光变量
varying vec4 vDiffuse; //接收从从顶点着色器的漫反射变量
varying vec4 vColor;

void main()                         
{           
   //给此片元从纹理中采样出颜色值             
   vec4 color = texture2D(sTexture_, vTextureCoord_); 
    alphaFinal =  color.a*uAlpha;
    gl_FragColor = vec4(color.rgb/color.a,alphaFinal); 
}         
		
     