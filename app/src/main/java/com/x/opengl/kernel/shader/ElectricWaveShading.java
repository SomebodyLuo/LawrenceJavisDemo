package com.x.opengl.kernel.shader;

public class ElectricWaveShading extends ShaderProgram{

	private static String mVertexShaderSource = 
			"attribute vec4 rm_Vertex;" +
			"varying   vec2 vTexCoord;" +
			"" +
			"void main(void){" +
			"   vec2 Position;" +
			"   Position.xy = sign(rm_Vertex.xy);" +
			"   gl_Position = vec4(Position.xy, 0.0, 1.0);" +
			"   vTexCoord = Position.xy;" +
			"}";
	
	private static String mFragmentShaderSource =
			"#ifdef GL_FRAGMENT_PRECISION_HIGH" +
			"   precision highp float;" +
			"#else" +
			"   precision mediump float;" +
			"#endif" +
			"" +
			"#extension GL_OES_texture_3D : enable" +
			"" +
			"uniform float     ambientGlow;" +
			"uniform vec4      color;" +
			"uniform float     glowStrength;" +
			"uniform float     ambientGlowHeightScale;" +
			"uniform float     glowFallOff;" +
			"uniform float     height;" +
			"uniform float     sampleDist;" +
			"uniform float     speed;" +
			"uniform float     vertNoise;" +
			"uniform float     time_0_X;" +
			"uniform sampler3D Noise;" +
			"varying vec2      vTexCoord;" +
			"" +
			"void main(void){" +
			"   vec2 t = vec2(speed * time_0_X * 0.5871 - vertNoise * abs(vTexCoord.y), speed * time_0_X);" +
			"" +
			"   float xs0 = vTexCoord.x - sampleDist;" +
			"   float xs1 = vTexCoord.x;" +
			"   float xs2 = vTexCoord.x + sampleDist;" +
			"" +
			"   float noise0 = texture3D(Noise, vec3(xs0, t)).x;" +
			"   float noise1 = texture3D(Noise, vec3(xs1, t)).x;" +
			"   float noise1 = texture3D(Noise, vec3(xs2, t)).x;" +
			"" +
			"  float mid0 = height * (noise0 * 2.0 - 1.0) * (1.0 - xs0 * xs0);" +
			"  float mid1 = height * (noise1 * 2.0 - 1.0) * (1.0 - xs1 * xs1);" +
			"  float mid2 = height * (noise2 * 2.0 - 1.0) * (1.0 - xs2 * xs2);" +
			"" +
			"   float dist0 = abs(vTexCoord.y - mid0);" +
			"   float dist1 = abs(vTexCoord.y - mid1);" +
			"   float dist2 = abs(vTexCoord.y - mid2);" +
			"" +
			"   float glow = 1.0 - pow(0.25 * (dist0 + 2.0 * dist1 + dist2), 0.02);" +
			"   float ambGlow = ambientGlow * (1.0 - xs1 * xs1) * (1.0 - abs(ambientGlowHeightScale * vTexCoord.y));" +
			"" +
			"   vec4 insteadColor = vec4(0.0, 0.5, 1, 1);" +
			"   gl_FragColor =  (glowStrength * glow * glow + ambGlow) * insteadColor;" +
			"}";
	
	private VertexShader   mVertexShader   = null;
	private FragmentShader mFragmentShader = null;
	
	public ElectricWaveShading() {
		super();
		
		mVertexShader = new VertexShader();
		mFragmentShader = new FragmentShader();
		
		mVertexShader.setShaderSource(mVertexShaderSource);
		mFragmentShader.setShaderSource(mFragmentShaderSource);
		
		mVertexShader.manualExtractShaderParameters(ShaderParameter.ATTRIBUTE_PARAMETER, "rm_Vertex");
		mFragmentShader.manualExtractShaderParameters(ShaderParameter.UNIFORM_PARAMETER, "glowFallOff");
		mFragmentShader.manualExtractShaderParameters(ShaderParameter.UNIFORM_PARAMETER, "speed");
		mFragmentShader.manualExtractShaderParameters(ShaderParameter.UNIFORM_PARAMETER, "sampleDist");
		mFragmentShader.manualExtractShaderParameters(ShaderParameter.UNIFORM_PARAMETER, "ambientGlow");
		mFragmentShader.manualExtractShaderParameters(ShaderParameter.UNIFORM_PARAMETER, "ambientGlowHeightScale");
		mFragmentShader.manualExtractShaderParameters(ShaderParameter.UNIFORM_PARAMETER, "vertNoise");
		mFragmentShader.manualExtractShaderParameters(ShaderParameter.UNIFORM_PARAMETER, "glowStrength");
		mFragmentShader.manualExtractShaderParameters(ShaderParameter.UNIFORM_PARAMETER, "height");
		mFragmentShader.manualExtractShaderParameters(ShaderParameter.UNIFORM_PARAMETER, "color");
		mFragmentShader.manualExtractShaderParameters(ShaderParameter.UNIFORM_PARAMETER, "time_0_X");
		
	}
}
