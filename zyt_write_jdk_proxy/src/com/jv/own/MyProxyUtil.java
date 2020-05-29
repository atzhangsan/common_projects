package com.jv.own;

import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class MyProxyUtil {

	private static final String l = "\n\r";
	private static final String t = "\t";
 
	public static Object newInstance(ClassLoader classLoader,Class[] interfaces,MyInvocationHandler handler) throws Exception{
		StringBuilder sb = new StringBuilder();
		StringBuilder sbImport = new StringBuilder();
		StringBuilder sbImplements = new StringBuilder();
		StringBuilder sbMethods = new StringBuilder();
 
		//############################################生成源代码############################################
		//包定义
		sb.append("package com.jv.own;").append(l+l);
		if(interfaces == null || interfaces.length==0){
			throw new Exception("接口不能为空");
		}
		//生成导入和implements
		for (Class anInterface : interfaces) {
			sbImport.append("import "+anInterface.getName()+";"+l);
			sbImplements.append(anInterface.getSimpleName()+",");
		}
		sbImport.append("import java.lang.reflect.Method;").append(l+l);
		sbImport.append("import com.jv.own.MyInvocationHandler;").append(l+l);
		sb.append(sbImport.toString());
		String temp = sbImplements.toString().substring(0,sbImplements.toString().lastIndexOf(","));
		//类定义
		sb.append("public class MyProxy implements ").append(temp).append("{").append(l).append(l);
		sb.append(t+"private MyInvocationHandler handler;").append(l);
		//构造函数
		sb.append(t+"public MyProxy(").append("MyInvocationHandler handler){").append(l);
		sb.append(t+t+"this.handler = handler;").append(l).append(t).append("}").append(l).append(l);
		//生成接口里面所有的
		for (Class anInterface : interfaces) {
			Method[] methods = anInterface.getMethods();
			for (Method method : methods) {
				String parameter = "";
				String parameterType = "";
				int i = 1;
				for(Class cl:method.getParameterTypes()){
					parameter += cl.getName() + " p" + i++ +",";
					parameterType += "Class.forName(\""+cl.getTypeName()+"\")"+",";
				}
				if(parameter!=null&&!parameter.equals("")) {
					parameter = parameter.substring(0, parameter.lastIndexOf(","));
					parameterType = parameterType.substring(0, parameterType.lastIndexOf(","));
				}
				sbMethods.append(t).append("public ").append(method.getReturnType().getName()).append(" ").append(method.getName())
						 .append("(").append(parameter).append("){").append(l+t+t);
				sbMethods.append("try{").append(l+t+t+t);
				String args = "Object[] args = new Object[]{";
				for(int j=1;j<i;j++){
					args += "p"+j+",";
				}
				args = args.substring(0,args.lastIndexOf(","))+"};";
				sbMethods.append(args).append(l+t+t+t);
 
				StringBuilder methodContent = new StringBuilder();
				//methodContent.append("Method method = Class.forName(\""+anInterface.getTypeName()+"\").getMethod(\""+method.getName()+"\","+parameterType+");");
				methodContent.append("Method method = "+anInterface.getName()+".class.getMethod(\""+method.getName()+"\","+parameterType+");");
				sbMethods.append(methodContent.toString()).append(l+t+t+t);
 
				if(method.getReturnType().getName().equals("void")){
					sbMethods.append("handler.invoke(this,method,args);").append(l+t+t);
				}else{
					sbMethods.append("return "+"("+method.getReturnType().getName()+")"+"handler.invoke(this,method,args);").append(l+t+t);
				}
				sbMethods.append("}catch(Throwable e){"+l+t+t+t+"return null;"+l+t+t+"}"+l+t);
				sbMethods.append("}").append(l);
			}
		}
		sb.append(sbMethods.toString());
 
		sb.append("}");
		System.out.println(sb.toString());
 
		//############################################将源代码写入磁盘文件############################################
		String filePath = MyProxyUtil.class.getResource("").getPath()  + "MyProxy.java";
		System.out.println(filePath);
		FileWriter fileWriter = new FileWriter(filePath);
		fileWriter.write(sb.toString());
		fileWriter.flush();
		fileWriter.close();
		//############################################编译源代码############################################
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable iterable = fileManager.getJavaFileObjects(filePath);
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, iterable);
		task.call();
		fileManager.close();
		//############################################将文件通过URLClassLocader或者自定义ClassLoader进行加载############################################
		URL[] urls = new URL[]{new URL("file:f\\\\projects\\own\\spring-framework-master\\proxy\\out\\production\\classes")};
 
		URLClassLoader urlClassLoader = new URLClassLoader(urls);
		Class clazz = urlClassLoader.loadClass("com.jv.own.MyProxy");
		//############################################实例化############################################
 
		//返回
		Constructor[] constructors = clazz.getConstructors();
		
		return constructors[0].newInstance(handler);
	}
}