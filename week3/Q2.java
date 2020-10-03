package fm.lizhi.payment.platform.admin.trade.controller;

import java.util.ArrayList;
import java.util.List;

public class Q2 {

    public static void main(String[] args) {
        WindowForm form = (WindowForm) new WindowForm("WINDOW窗口").addChild(new Picture("LOGO图片"))
                .addChild(new Button("登录"))
                .addChild(new Button("注册"))
                .addChild(new Frame("FRAME1")
                        .addChild(new TextBox("文本框"))
                        .addChild(new Lable("密码"))
                        .addChild(new PasswordBox("密码框"))
                        .addChild(new CheckBox("复选框"))
                        .addChild(new TextBox("记住用户名"))
                        .addChild(new LinkLable("忘记密码")));
        form.display();
    }

    private abstract static class Component {

        private List<Component> children = new ArrayList();

        protected String componentName;

        public Component(String componentName){
            this.componentName = componentName;
        }

        public void display(){
            this.print();
            children.forEach(c->{
                c.display();
            });
        }

        public Component addChild(Component component){
            this.children.add(component);
            return this;
        }

        public void print() {
            System.out.println(" print " + this.getClass().getSimpleName() + "(" + componentName + ")");
        }
    }

    public static class WindowForm extends Component{

        public WindowForm(String componentName) {
            super(componentName);
        }

    }

    public static class Picture extends Component{

        public Picture(String componentName) {
            super(componentName);
        }

    }
    public static class Button extends Component{

        public Button(String componentName) {
            super(componentName);
        }

    }
    public static class Frame extends Component{

        public Frame(String componentName) {
            super(componentName);
        }

    }
    public static class Lable extends Component{

        public Lable(String componentName) {
            super(componentName);
        }

    }
    public static class TextBox extends Component{

        public TextBox(String componentName) {
            super(componentName);
        }

    }
    public static class PasswordBox extends Component{

        public PasswordBox(String componentName) {
            super(componentName);
        }

    }
    public static class CheckBox extends Component{

        public CheckBox(String componentName) {
            super(componentName);
        }

    }
    public static class LinkLable extends Component{

        public LinkLable(String componentName) {
            super(componentName);
        }

    }

}
