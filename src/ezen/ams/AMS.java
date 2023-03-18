package ezen.ams;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * 은행 계좌관리 애플리케이션 데이터 저장소(Data Source)로 File 이용 -> Database
 * 
 * @author 김기정
 * @Date 2023. 2. 1.
 */
public class AMS {
	
	/** 프레임 화면 중앙 배치 */
	public static void setCenterScreen(Frame frame) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int screenWidth = toolkit.getScreenSize().width;
		int screenHeight = toolkit.getScreenSize().height;
		int centerX = (screenWidth - frame.getWidth()) / 2;
		int centerY = (screenHeight - frame.getHeight()) / 2;
		frame.setLocation(centerX, centerY);
	}

	public static void main(String[] args) {
		Frame frame = new Frame("::: " + Account.BANK_NAME + " 계좌 관리 :::");
		try {
			// 계좌 정보 파일 저장 및 관리
			
			AccountRepository repository = new ObjectAccountRepository();
//			AccountRepository repository = new FileAccountRepository();
//			AccountRepository repository = new DatabaseAccountRepository();
			
			// GUI 생성
			AccountPanel panel = new AccountPanel(repository);
			panel.initLayout();
			panel.addEventListener();
			frame.add(panel, BorderLayout.CENTER);
			frame.setSize(500, 450);
			setCenterScreen(frame);
			frame.setVisible(true);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			
			System.out.println("Git Test");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "프로그램을 실행할 수 없습니다.", JOptionPane.ERROR_MESSAGE);
		}
	}

}
