package cn.crap.utils;

public class OpenFileUtil {
	private static final String DEFAULT_BUDDY_PATH = "D:\\Program Files\\SF_Buddy\\eclipse.exe";

	// 用 Java 调用windows系统的exe文件，比如notepad，calc之类
	public static void openWindowsExe() {
		final Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			final String command = "notepad";// 记事本
			process = runtime.exec(command);
		} catch (final Exception e) {
			System.out.println("Error win exec!");
		}
	}

	// 调用其他的可执行文件，例如：自己制作的exe，或是 下载 安装的软件.
	public static void openBuddyExe(String userName, String userPwd, String operationType,String registration_id) throws Exception {
		openExe(DEFAULT_BUDDY_PATH + " " + userName + " " + userPwd + " " + operationType + " true "+registration_id);
	}

	// 调用其他的可执行文件，例如：自己制作的exe，或是 下载 安装的软件.
	public static void openExe(String absolutePath) throws Exception {
		final Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec(absolutePath);
		} catch (final Exception e) {
			System.out.println("Error exec!");
			e.printStackTrace();
			throw e;
		}
	}

	// 打开其他任意格式的文件，比如txt,word等
	public static void openFile() {
		final Runtime runtime = Runtime.getRuntime();
		Process process = null;//
		final String cmd = "rundll32 url.dll FileProtocolHandler file://F:\\ECT项目资料\\建立EMF工程.txt";
		try {
			process = runtime.exec(cmd);
		} catch (final Exception e) {
			System.out.println("Error exec!");
		}
	}
}
