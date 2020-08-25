package src;
import src.MyDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.*;
import java.time.format.DateTimeParseException;

public class DateCalculator
{
	private JFrame frame = new JFrame("DateCalculator");
	private JPanel panelForNumber = new JPanel();
	private JPanel panelForOperator = new JPanel();
	private JTextField textFieldForResult = new JTextField(20);
	private JButton buttonCE = new JButton("CE");	// clear a input
	private JButton buttonClear = new JButton("C");	// clear all input
	private JButton buttonDel = new JButton("<-");	// backspace
	private JButton buttonPlus = new JButton("+");
	private JButton buttonMinus = new JButton("-");
	private JButton buttonFormat = new JButton("/");
	private JButton buttonEqu = new JButton("=");
	private JButton button0 = new JButton("0");

	private MyDate curDate = null;
	private Operator curOpeartor = Operator.Null;
	private boolean finished = true;

	enum Operator {
		Plus,
		Minus,
		Null;
	}

	private final static int[] getKeyNumber() {
		int arr[] = {7, 8, 9, 4, 5, 6, 1, 2, 3};
		return arr;
	}

	// transform String in JTextField to MyDate and save as this.curDate
	private void pushTextToDate() {
		this.curDate = new MyDate(this.textFieldForResult.getText());
	}

	/**
	 * @return the result of calc
	*/
	private String add() {
		String dateStr = this.textFieldForResult.getText();
		if (dateStr.split("/").length == 3) {
			return this.curDate.add(dateStr);
		} else {
			try {
				Long days = Long.valueOf(dateStr);
				return this.curDate.add(days);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return "Error Number Format";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Error Input";
	}

	private String minus() {
		String dateStr = this.textFieldForResult.getText();
		if (dateStr.split("/").length == 3) {
			return this.curDate.minus(dateStr);
		} else {
			try {
				Long days = Long.valueOf(dateStr);
				return this.curDate.minus(days);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return "Error Number Format";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Error Input";
	}

	private String showDaysOfWeek() {
		return this.curDate.toString();
	}

	private void showResult() {
		switch (this.curOpeartor) {
		case Plus:
			this.textFieldForResult.setText(this.add());
			break;
		case Minus:
			this.textFieldForResult.setText(this.minus());
			break;
		case Null:
			this.textFieldForResult.setText(this.showDaysOfWeek());
			break;
		}
	}

	private void addStrToTextField(String s) {
		this.startCalculator();
		this.textFieldForResult.setText(this.textFieldForResult.getText() + s);
	}

	private void startCalculator() {
		if (this.finished) {
			this.onButtonCEClick();
			this.finished = false;
		}
	}

	private void endOneCalculator() {
		this.curOpeartor = Operator.Null;
		this.curDate = null;
		this.finished = true;
	}

	private void dealFormatError() {
		this.textFieldForResult.setText("Error Date Format!");
		this.finished = true;
	}

	private void onButtonPlus() {
		this.pushTextToDate();
		this.curOpeartor = Operator.Plus;
		this.finished = true;
	}

	private void onButtonMinus() {
		this.pushTextToDate();
		this.curOpeartor = Operator.Minus;
		this.finished = true;
	}

	private void onButtonEquClick() {
		if (this.finished) return;	// prevent pressed at showing result
		try {
			if (curDate == null && curOpeartor == Operator.Null) {
				pushTextToDate();
			}
			showResult();	// calc in there actually
		} catch (DateTimeParseException e) {
			this.dealFormatError();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			endOneCalculator();
		}
	}

	// clear text in the JTextField
	private void onButtonCEClick() {
		this.textFieldForResult.setText("");
	}

	// stop a calculator
	private void onButtonClearClick() {
		this.onButtonCEClick();
		this.endOneCalculator();
	}

	// delete a character
	private void onButtonDelClick() {
		String current = this.textFieldForResult.getText();
		if (!current.isEmpty()) {
			this.textFieldForResult.setText(current.substring(0, current.length() - 1));
		}
	}

	public DateCalculator() {
		// '+'
		this.buttonPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == buttonPlus) {
					onButtonPlus();
				}
			}
		});

		// '-'
		this.buttonMinus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == buttonMinus) {
					onButtonMinus();
				}
			}
		});

		// '/'
		this.buttonFormat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == buttonFormat) {
					addStrToTextField("/");
				}
			}
		});

		// '='
		this.buttonEqu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == buttonEqu) {
					onButtonEquClick();
				}
			}
		});

		// "C"
		this.buttonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == buttonClear) {
					onButtonClearClick();
				}
			}
		});

		// "CE"
		this.buttonCE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == buttonCE) {
					onButtonCEClick();
				}
			}
		});

		// '<-'
		this.buttonDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == buttonDel) {
					onButtonDelClick();
				}
			}
		});

		// textFieldForResult
		Font font = new Font("楷体", Font.PLAIN, 18);
		this.textFieldForResult.setEditable(false);
		this.textFieldForResult.setFont(font);
		this.textFieldForResult.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char pressed = e.getKeyChar();
				System.out.print(pressed);
				// It's amazing that connot invoke '/'.equals(char)
				if (Character.isDigit(pressed) || pressed == '/') {
					addStrToTextField(String.valueOf(pressed));
				} else {
					switch (pressed) {
					case '+':
						onButtonPlus();
						break;
					case '-':
						onButtonMinus();
						break;
					case '=':
					case '\n':	// '=' or 'return'
						onButtonEquClick();
						break;
					case '\b':	// backspace
						onButtonDelClick();
						break;
					default:
						break;
					}
				}
			}
		});

		this.panelForOperator.setLayout(new GridLayout(4, 1, 10, 10));
		this.panelForOperator.add(this.buttonPlus);
		this.panelForOperator.add(this.buttonMinus);
		this.panelForOperator.add(this.buttonFormat);
		this.panelForOperator.add(this.buttonEqu);

		this.panelForNumber.setLayout(new GridLayout(5, 3, 10, 10));
		this.panelForNumber.add(this.buttonClear);
		this.panelForNumber.add(this.buttonCE);
		this.panelForNumber.add(this.buttonDel);

		JButton tempBut = null;
		for (int i : DateCalculator.getKeyNumber()) {
			tempBut = new JButton(String.valueOf(i));
			this.panelForNumber.add(tempBut);
			tempBut.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// if (e.getSource() == tempBut) {
						addStrToTextField(String.valueOf(i));
					// }
				}
			});
		}

		this.panelForNumber.add(this.button0);
		this.button0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == button0) {
					addStrToTextField("0");
				}
			}
		});

		// frame
		this.frame.setLayout(new BorderLayout(25, 25));
		this.frame.add(this.panelForNumber, BorderLayout.CENTER);
		this.frame.add(this.panelForOperator, BorderLayout.EAST);
		this.frame.add(this.textFieldForResult, BorderLayout.NORTH);
		this.frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.frame.pack();
		this.frame.setVisible(true);
	}

	public static void main(String args[]) {
		new DateCalculator();
	}
}