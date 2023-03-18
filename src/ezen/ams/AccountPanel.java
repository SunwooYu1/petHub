package ezen.ams;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
 * GridBagLayout을 이용한 프레임 레이아웃
 * @Author 김재훈
 * @Date 2023. 1. 17.
 *       발생 버그: 1) getText로 신규계좌 등록하면 계좌번호가 아닌 예금주명이 먼저 나옴=> 해결. account에 new
 *       Account로 배열 인스턴스를 생성하는데 순서가 이름을 가장 먼저 둠. 그러니 그 순서대로 나온것. 역시 컴퓨터는 거짓말을 안 함 
 *       2) getText로 등록된 계좌는 계좌번호 조회로 검색이 안 됨. => 1)에서 해결 
 *       3) getText로 등록된 계좌를
 *       예금주명으로 조회하면 '해당 계좌가 없습니다' 출력. 수동으로 등록한 계좌는 검색은 되는데, 나머지 등록된 계좌 수 만큼 '해당
 *       계좌가 없습니다'가 같이 출력 => 해결. else if에서 해당 안되는 계좌는 continue로 넘어가게 만듦. 
 *       4) 공란이 하나라도 있는 상태에서 상태에서 등록버튼 누르면 NumberFormatException 생김. => 해결? try catch로 묶었음 => 공란 혹은 숫자인데 문자거나 이러면 이게 뜸. 로직으로 해결 필요
 * NumberFormatException : 숫자가 들어와야 할 자리에 숫자 아닌게 들어오면 뜨는 예외. 조건문 혹은 로직을 통하여 해결하고 싶다.\
 * 혹은 isDigit 써보기?  https://velog.io/@ddingmun8/JAVA-%EB%AC%B8%EC%9E%90%EC%97%B4%EC%9D%B4-%EC%88%AB%EC%9E%90%EC%9D%B8%EC%A7%80-%ED%8C%90%EB%B3%84%ED%95%98%EA%B8%B0
 */
public class AccountPanel extends Panel {
	// 계좌정보 파일 저장 객체
	AccountRepository repository; 

	GridBagLayout gridBagLayout;
	Label nameLabel, accountNumLabel, accountTypeLabel, pwdLabel, InputMoneyLabel, rentMoneyLabel, accountListLabel, unitLabel;
	TextField nameTF, accountNumTF, pwdTF, InputMoneyTF, rentMoneyTF;
	Button searchButton, searchButton2, deleteButton;
	TextArea contentsArea;
	Panel buttonPanel = new Panel();
	Button enterButton, searchAllButton, saveButton;
	Choice choice;
	
	public AccountPanel() {}

	public AccountPanel(AccountRepository repository) {
		this.repository = repository;
		accountTypeLabel = new Label("계좌종류");
		nameLabel = new Label("예금주명");
		nameTF = new TextField();
		accountNumLabel = new Label("계좌번호");
		accountNumTF = new TextField();
		searchButton = new Button("조 회");
		searchButton2 = new Button("조 회");
		deleteButton = new Button("삭 제");
		pwdLabel = new Label("비밀번호");
		pwdTF = new TextField();
		pwdTF.setEchoChar('*');
		InputMoneyLabel = new Label("입금금액");
		InputMoneyTF = new TextField();
		rentMoneyLabel = new Label("대출금액");
		rentMoneyTF = new TextField();
		choice = new Choice();
		choice.add("입출금계좌");
		choice.add("마이너스계좌");
		enterButton = new Button("신규 등록");
		searchAllButton = new Button("전체 조회");
		saveButton = new Button("    저장    ");
		buttonPanel.add(enterButton);
		buttonPanel.add(searchAllButton);
		buttonPanel.add(saveButton);
		accountListLabel = new Label("계좌 목록");
		unitLabel = new Label("(단위 : 원)");
		contentsArea = new TextArea();
	}

	// 현재 프레임의 배치 설정 메소드
	public void initLayout() {
		gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		add(accountTypeLabel, 0, 0, 1, 1, 0);
		add(choice, 1, 0, 2, 1, 1);
		add(nameLabel, 0, 1, 1, 1, 0);
		add(nameTF, 1, 1, 2, 1, 1);
		add(accountNumLabel, 0, 2, 1, 1, 0);
		add(accountNumTF, 1, 2, 2, 1, 1);
		add(searchButton, 3, 1, 1, 1, 0.1);
		add(searchButton2, 3, 2, 1, 1, 0.1);
		add(deleteButton, 4, 2, 1, 1, 0.1);
		add(pwdLabel, 0, 3, 1, 1, 0);
		add(pwdTF, 1, 3, 2, 1, 1);
		add(InputMoneyLabel, 0, 4, 1, 1, 0);
		add(InputMoneyTF, 1, 4, 2, 1, 1);
		add(rentMoneyLabel, 0, 5, 1, 1, 0);
		add(rentMoneyTF, 1, 5, 2, 1, 1);
		add(buttonPanel, 0, 6, 6, 1, 1);
		add(accountListLabel, 0, 7, 1, 1, 0);
		add(unitLabel, 5, 7, 1, 1, 0);
		add(contentsArea, 0, 8, 6, 2, 1);
		
		// 실행 시 전체 계좌 출력
		showAccounts();
	}

	// GridBagLayout을 이용한 컴포넌트 배치
	private void add(Component component, int gridx, int gridy, int gridwidth, int gridheight, double weightx) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.weightx = weightx; // 가중치 0.1
		constraints.weighty = 0.0;

		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		gridBagLayout.setConstraints(component, constraints);
		add(component);
	}
	
	/** 컴포넌트 초기화 */
	public void setComponentReset() {
		choice.select(0);
		nameTF.setText("");
		accountNumTF.setText("");
		pwdTF.setText("");
		InputMoneyTF.setText("");
		rentMoneyTF.setText("");
		rentMoneyTF.setEditable(false);
		rentMoneyTF.setEnabled(false);
	}
	
	/** 계좌종류 선택 */
	private void selectAccountType() {
		if(choice.getSelectedItem().equals("입출금계좌")) {
			rentMoneyTF.setEditable(false);
			rentMoneyTF.setEnabled(false);
		}else {
			rentMoneyTF.setEditable(true);
			rentMoneyTF.setEnabled(true);
		}
	}
	
	/** TextArea 헤더 출력 */
	private void printHeader() {
		String title = String.format("| %-12s | %-15s | %-5s | %11s | %11s |\n", "계좌종류", "계좌번호", "예금주명", "현재잔액", "대출금액");
		contentsArea.append("---------------------------------------------------------------------------\n");
		contentsArea.append(title);
		contentsArea.append("===========================================================================\n");
	}
	
	/** 계좌 정보 출력 */
	private void showAccount(Account account) {
		String accountType = "입출금계좌";
		String accountNumber = account.getAccountNumber();
		String accountOwner = account.getAccountOwner();
		long restMoney = account.getRestMoney();
		long borrowMoney = 0;
		
		String typeSize = "%-10s";
		String borrowSize = "%,19d";
		if(account instanceof MinusAccount) {
			accountType = "마이너스계좌";
			borrowMoney = ((MinusAccount)account).getBorrowMoney();
			typeSize = "%-8s";
			borrowSize = "%,15d";
		}
		String accountInfo = String.format("| "+typeSize+" | %-16s | %-7s | %+,15d | "+borrowSize+" |\n", accountType, accountNumber, accountOwner, restMoney, borrowMoney);
		contentsArea.append(accountInfo);
		contentsArea.append("---------------------------------------------------------------------------\n");
	}
	
	/** 메시지 출력 */
	private void showMessage(String message) {
		contentsArea.setText(message);
	}
	
	/** 전체 계좌목록 출력 */
	private void showAccounts() {
		contentsArea.setText("");
		printHeader();
		try {
			List<Account> list = repository.getAccounts();
			for (Account account : list) {
				showAccount(account);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 신규계좌 등록 */
	private void createAccount() {
		Account newAccount = null;
		String accountNumber = accountNumTF.getText();
		String accountOwner = nameTF.getText();
		String password = pwdTF.getText();
		String inputMoney = InputMoneyTF.getText();
		String borrowMoney = null;
		
		if(Valiator.isEmpty(accountOwner)) {
			showErrorMessage(nameTF, "예금주명을 입력하여 주세요.");
			return;
		}
		
		if(Valiator.isEmpty(accountNumber)) {
			showErrorMessage(accountNumTF, "계좌번호를 입력하여 주세요.");
			return;
		}
		
		if(Valiator.isEmpty(password)) {
			showErrorMessage(pwdTF, "비밀번호를 입력하여 주세요.");
			return;
		}
		
		if(!Valiator.isNumber(password)) {
			showErrorMessage(pwdTF, "비밀번호는 숫자형식입니다.");
			return;
		}
		
		if(Valiator.isEmpty(inputMoney)) {
			showErrorMessage(InputMoneyTF, "입금금액을 입력하여 주세요.");
			return;
		}
				
		if(!Valiator.isNumber(inputMoney)) {
			showErrorMessage(InputMoneyTF, "입금금액은 숫자형식입니다.");
			return;
		}
		
		// 입출금계좌 신규 등록
		if(choice.getSelectedItem().equals("입출금계좌")) {
			newAccount = new Account(accountNumber, accountOwner, Integer.parseInt(password), Long.parseLong(inputMoney));
		}else { // 마이너스 계좌 신규 등록
			borrowMoney = rentMoneyTF.getText();
			if(Valiator.isEmpty(borrowMoney)) {
				showErrorMessage(rentMoneyTF, "대출금액을 입력하여 주세요.");
				return;
			}
			if(!Valiator.isNumber(borrowMoney)) {
				showErrorMessage(rentMoneyTF, "대출금액은 숫자형식입니다.");
				return;
			}
			newAccount = new MinusAccount(accountNumber, accountOwner, Integer.parseInt(password), Long.parseLong(inputMoney), Long.parseLong(borrowMoney));
		}
		
		try {
			repository.addAccount(newAccount);
			showMessage("※ 신규계좌가 등록되었습니다 ※");
			setComponentReset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 텍스트필드에 입력 오류 메시지 출력 */
	private void showErrorMessage(TextComponent component, String errorMessage) {
		component.setForeground(Color.RED);
		component.setText(errorMessage);
	}
	
	/** 파일 저장 */
	private void saveFile() {
		try {
			((ObjectAccountRepository)repository).saveFile();
			showMessage("계좌정보를 파일로 저장하였습니다..");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/** 각각의 이벤트 소스에 이벤트 리스너 연결(등록) */
	public void addEventListener() {
		// 계좌 선택
		choice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					selectAccountType();
				}
			}
		});
		
		// 신규계좌 등록
		enterButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createAccount();
			}
		});
		// 전체계좌 조회
		searchAllButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showAccounts();
			}
		});
		
		// 저장
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile();

			}
		});

		nameTF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				nameTF.setForeground(Color.BLACK);
				nameTF.setText("");
			}

		});
		accountNumTF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				accountNumTF.setForeground(Color.BLACK);
				accountNumTF.setText("");
			}
		});
		
		pwdTF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pwdTF.setForeground(Color.BLACK);
				pwdTF.setText("");
			}
		});
		
		InputMoneyTF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				InputMoneyTF.setForeground(Color.BLACK);
				InputMoneyTF.setText("");
			}
		});
		
		rentMoneyTF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				rentMoneyTF.setForeground(Color.BLACK);
				rentMoneyTF.setText("");
			}
		});

	}
	
	
}
