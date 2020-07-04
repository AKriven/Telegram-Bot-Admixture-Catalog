import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.File;

public class Bot extends TelegramLongPollingBot {

   java.io.File file = new java.io.File("\\resources\\PdfCatalog\\CENTRAMENT AIR 202.4.pdf");
    File fileCatalog = new File("./src/main/resources/PdfCatalog/");
    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }
    private File searchFile(Message message) {
        message.getText();
        String name;
        name = message.getText().replaceAll("[^\\d]", "");
        File searchedfile = null;
        for(File item : fileCatalog.listFiles()){
            String nameForEquals = item.getName().replaceAll("[^\\d]", "");
            if(name.equals(nameForEquals)){
                searchedfile = new File(item.getAbsolutePath());
                break;
            }

        }
        return searchedfile;
    }


    private void sendDocUploadingAFile(Message message, java.io.File save,String caption) {

        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(message.getChatId().toString());
        sendDocument.setNewDocument(save);
        sendDocument.setCaption(caption);
        try {
            sendDocument(sendDocument);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

   private String sendList(){
        String fullList = null;
        String list = "Список добавок: "+"\n";
        for(File item : fileCatalog.listFiles()) {
            list =list + item.getName() + "\n";
        }

        return fullList=list;
   }

    private void sendMsg(Message message, String text) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "Бот отправляет .pdf файл описания добавки. " +
                            "Введите название добавки или цифры для получения описания." +
                            "Получить список добавок в каталоге\"/list\".");
                    break;
                case"/start":
                    sendMsg(message, "Бот отправляет .pdf файл описания добавки. " +
                            "Введите название добавки или цифры для получения описания." +
                            " Желательно только цифры, но не обязательно. Бот пока не ищет" +
                            " файлы в которых нет цифр.Пример: PF3100, Тф173, П40, 19." +
                            "Получить список добавок в каталоге\"/list\"");
                    break;
                case "1":
                    sendDocUploadingAFile(message, file, "Бульк");
                    break;
                case "Аня":
                    sendMsg(message,"А я знаю что ты Аня, меня только что научили реагировать на твое имя!");
                case"/list":
                    sendMsg(message, sendList());
                    break;
                default:
                    sendDocUploadingAFile(message,searchFile(message), "Получите распишитесь");
            }
        }


    }



    @Override
    public String getBotUsername() {
        return "AdmixCatalogBot";
    }

    @Override
    public String getBotToken() {
        return "1040538363:AAGKFPzb6QkbFDIsJTOFqfvocPVEstQj2YI";
    }
}
