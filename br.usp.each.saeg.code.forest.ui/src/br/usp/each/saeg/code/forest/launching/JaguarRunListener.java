package br.usp.each.saeg.code.forest.launching;

import java.io.IOException;

import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import br.usp.each.saeg.jaguar.Jaguar;
import br.usp.each.saeg.jaguar.jacoco.JacocoTCPClient;

public class JaguarRunListener extends TestRunListener {

	private Jaguar jaguar;

	private JacocoTCPClient tcpClient;

	private boolean currentTestFailed;
	
	public JaguarRunListener() {
		super();
	}

	public JaguarRunListener(Jaguar jaguar, JacocoTCPClient tcpClient) {
		this.jaguar = jaguar;
		this.tcpClient = tcpClient;
	}

	public void testStarted(Description description) throws Exception {
		currentTestFailed = false;

		jaguar.increaseNTests();
	}

	public void testFailure(Failure failure) throws Exception {
		currentTestFailed = true;

		jaguar.increaseNTestsFailed();
	}

	public void testFinished(Description description) throws Exception {

	}

	@Override
	public void testCaseFinished(ITestCaseElement testCaseElement) {
		print(testCaseElement);
		try {
			jaguar.collect(tcpClient.read(), currentTestFailed);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void print(ITestCaseElement testCaseElement) {
		String result = currentTestFailed ? "Failed" : "Passed";
		System.out.println("Test " + testCaseElement.getTestClassName() + "." + testCaseElement.getTestMethodName() + ": " + result);
	}

}
