import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import org.jsoup.Jsoup;

import java.io.IOException;


public class NasaBot {
    public static void main(String[] args) {
        //5681014629:AAHoWmRXZ4o_4wl1Ymi-U-nP66Im23zABKQ
        // Create your bot passing the token received from @BotFather

            TelegramBot bot = new TelegramBot("5681014629:AAHoWmRXZ4o_4wl1Ymi-U-nP66Im23zABKQ");


        bot.setUpdatesListener(updates -> {
            updates.forEach(upd -> {
                try {
                    System.out.println(upd);
                    long chatId = upd.message().chat().id();
                    //logic
                    String date = upd.message().text(); //date=2022-08-09
                    String jsonString = Jsoup.connect("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=" + date)
                            .ignoreContentType(true)
                            .execute()
                            .body();
                    ObjectMapper objectMapper = new ObjectMapper();
                    var jsonNode = objectMapper.readTree(jsonString);
                    String imageUrl = jsonNode.get("url").asText();
                    String explanation = jsonNode.get("explanation").asText();
                    String result = imageUrl + "\n" + explanation;
                    //send response
                    SendMessage request = new SendMessage(chatId, result);
                    bot.execute(request);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }

}

