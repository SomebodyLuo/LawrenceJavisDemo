precision mediump float;
uniform sampler2D sTexture;//纹理内容数据
uniform float uAlpha;

varying vec2 vTextureCoord; //接收从顶点着色器过来的参数
varying vec4 vAmbient; //接收从从顶点着色器的环境光变量
varying vec4 vDiffuse; //接收从从顶点着色器的漫反射变量
varying vec4 vColor;

void main()                         
{           
   //给此片元从纹理中采样出颜色值             
   vec4 color = texture2D(sTexture, vTextureCoord); 
   
   //最终颜色
   //vec4 finalColor=vec4(color.rgb,color.a*uAlpha); 
   //给此片元颜色值
   
   
   // gl_FragColor = vec4(color.rgb,color.a*uAlpha) * (vDiffuse + vAmbient);
    gl_FragColor = vec4(color.rgb,color.a*uAlpha) ;
   //gl_FragColor = vec4(color.rgb,color.a*uAlpha) * (vAmbient);
   //	 gl_FragColor = vColor;
    
}         
		
//		void main() {           
//			  
//			   vec2      v2[9]; 
//			   float    value[9];
//			   float scaleFactor = 0.0;//给出最终求和时的加权因子(为调整亮度)
//			   
//			   //给出卷积内核中各个元素对应像素相对于待处理像素的纹理坐标偏移量
//			     v2[0] = vec2(-1,-1);      v2[1] = vec2(0,-1);   v2[2] = vec2(1,-1);
//			     v2[3] = vec2(-1,-0);      v2[4] = vec2(0,0);      v2[5] = vec2(1,0);
//			     v2[6] = vec2(-1,1);      v2[7] = vec2(0,1);      v2[8] = vec2(1,1);
//			     
//			     value[0] = 1.0;      value[1] = 2.0;      value[2] = 1.0;
//			     value[3] = 2.0;      value[4] = 4.0;      value[5] = 2.0;
//			     value[6] = 1.0;      value[7] = 2.0;      value[8] = 1.0;
//			             
//			   	 scaleFactor = 1.0/16.0;
//			   
//				vec4 sum;//最终的颜色和
//				float bite = 90.0;
//				
//				sum = sum + value[0] * texture2D(sTexture, vTextureCoord.st + v2[0].xy/bite);
//				sum = sum + value[1] * texture2D(sTexture, vTextureCoord.st + v2[1].xy/bite);
//				sum = sum + value[2] * texture2D(sTexture, vTextureCoord.st + v2[2].xy/bite);
//				sum = sum + value[3] * texture2D(sTexture, vTextureCoord.st + v2[3].xy/bite);
//				sum = sum + value[4] * texture2D(sTexture, vTextureCoord.st + v2[4].xy/bite);
//				sum = sum + value[5] * texture2D(sTexture, vTextureCoord.st + v2[5].xy/bite);
//				sum = sum + value[6] * texture2D(sTexture, vTextureCoord.st + v2[6].xy/bite);
//				sum = sum + value[7] * texture2D(sTexture, vTextureCoord.st + v2[7].xy/bite);
//				sum = sum + value[8] * texture2D(sTexture, vTextureCoord.st + v2[8].xy/bite);
//					
//			   vec4 color = sum * scaleFactor; 
//			   
//			   gl_FragColor = vec4(color.rgb,color.a*uAlpha);  //进行亮度加权后将最终颜色传递给管线
//			}         
     