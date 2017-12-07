uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix; //模型视图矩阵

uniform vec3 uLightLocation;

attribute vec2 aTexCoor;    //顶点纹理坐标
attribute vec3 aPosition;  //顶点位置
attribute vec4 aColor;
attribute vec3 aNormal;

varying vec2 vTextureCoord;  //用于传递给片元着色器的变量
varying vec4 vAmbient; //用于传递给片元着色器的环境光变量
varying vec4 vDiffuse; //用于传递给片元着色器的漫反射变量
varying vec4 vColor;

//散射光光照计算的方法
//法向量
//散射光计算结果
//散射光强度
//光源位置
void pointLigh ( in vec3 normal,inout vec4 diffuse,in vec3 lightLocation,in vec4 lightDiffuse){

  vec3 normalTarget=aPosition+normal;					//计算变换后的法向量
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal);					//对法向量规格化
//计算从表面点到光源位置的向量vp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);
  vp=normalize(vp);									//规格化vp
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//求法向量与vp向量的点积与0的最大值
  diffuse=lightDiffuse*nDotViewPosition;			//计算散射光的最终强度
  
  //diffuse = vec4(0,0,1,1);
}
void main()     
{                            		
   
   //vAmbient = vec4(0.75,0.75,0.75,1.0);
      
   vTextureCoord = aTexCoor;//将接收的纹理坐标传递给片元着色器
   vColor = aColor;
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
   
   vec4 diffuseTemp=vec4(0.0,0.0,0.0,0);  
   pointLigh(normalize(aNormal), diffuseTemp, uLightLocation, vec4(0.8,0.8,0.8,1));
   //point(diffuseTemp);
   vDiffuse=diffuseTemp;        
   vAmbient = vec4(0.4,0.4,0.4,1);					//计算环境光最终强度
   
}                      