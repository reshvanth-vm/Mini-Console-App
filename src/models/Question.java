package models;

public abstract class Question {
    protected String question;

    protected Question(String question) {
        this.question = question;
    }

    public abstract boolean isCorrect(String ans);

    public static class MCQ extends Question {
        private String[] options;
        private int correctOption;

        public MCQ(String question, String[] options, int correctOption) {
            super(question);
            this.options = options;
            this.correctOption = correctOption;
        }

        @Override
        public boolean isCorrect(String ans) {
            try {
                return correctOption == Integer.parseInt(ans);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public String toString() {
            String nl = System.lineSeparator();
            String string = question + nl;
            for (int i = 1; i <= options.length; i++)
                string += i + ". " + options[i - 1] + nl;
            return string;
        }
    }

    public static class Essay extends Question {
        private String[] keywords;

        public Essay(String question, String[] keywords) {
            super(question);
            this.keywords = keywords;
        }

        @Override
        public boolean isCorrect(String ans) {
            for (String keyword : keywords)
                if (ans.contains(keyword))
                    return true;
            return false;
        }

        @Override
        public String toString() {
            return question.concat(System.lineSeparator());
        }
    }
}
