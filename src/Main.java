import javassist.*;
import java.io.FileInputStream;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPool pool	= ClassPool.getDefault();
		String file		= args[0];
		
		
		try {
			Manifest Manifest		= new JarInputStream(new FileInputStream(file)).getManifest();
			String BuildTime		= Manifest.getMainAttributes().getValue("Build-Time");
			
	        pool.insertClassPath(file);
	        
			String GroupChat	= pool.get("base.GroupChatStandalone").getSuperclass().getName();
			String Version		= null;
			String ProtocolTree	= null;
			
			CtClass Context = pool.get(GroupChat);
			for(CtField f : Context.getFields()) {
				Object value	= f.getConstantValue();
				
				if(value instanceof String) {
					Pattern pattern = Pattern.compile("V([0-9.]+)([a-z]+)");
					Matcher matcher = pattern.matcher((String) f.getConstantValue());
					
					if(matcher.find()) {
						Version = matcher.group(0);
					}
					
					pattern = Pattern.compile("([0-9\\-]+);(.*);");
					matcher = pattern.matcher((String) f.getConstantValue());
					
					if(matcher.find()) {
						ProtocolTree = matcher.group(0);
					}
				}
			}

			System.out.println("{\n" +
			"	\"GroupChat\":\t\"" + GroupChat + "\",\n" +
			"	\"ProtocolTree\":\t\"" + ProtocolTree + "\",\n" +
			"	\"Version\":\t\"" + Version + "\",\n" +
			"	\"BuildTime\":\t\"" + BuildTime + "\"\n" +
			"}");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
