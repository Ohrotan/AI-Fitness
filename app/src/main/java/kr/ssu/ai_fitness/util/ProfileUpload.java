package kr.ssu.ai_fitness.util;

import android.util.Log;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.url.URLs;

public class ProfileUpload {
    private int serverResponseCode;

    String rtmsg="";

    public String upload(InputStream is, Member info) {

        //여기부터는 설정 부분
        HttpURLConnection conn = null; //HttpURLConnection 생성
        DataOutputStream dos = null; //DataOutputStream 구하기
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 100 * 1024 * 1024; //100mb
        Log.i("Huzza", "Member2 : " + info.toString());

        try {
            URL url = new URL(URLs.URL_SIGNUP);//URL 생성
            conn = (HttpURLConnection) url.openConnection();//URL 연결

            //HttpURLConnection 설정
            conn.setDoInput(true);//InputStream으로 서버로부터 응답을 받겠다는 옵션
            conn.setDoOutput(true);//OutputStream으로 POST 데이터를 넘겨주겠다는 옵션
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");//요청 방식 선택(GET, POST)

            //다수개의 request/response에 대해서도 기존에 맺은 TCP 커넥션을 재사용할 수 있도록 한다.
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            //보낼 type을 multipart로 설정함. boundary는 각 인자를 구분하기 위한 구분자 역할.
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            //request header값 세팅 setrequestProperty(String key, String value)
            conn.setRequestProperty("myFile", info.getImage());


            //DataOutputStream 구하기. 실제 서버로 request body에 넣어서 전달되어야하는 내용을 설정하기 위해 필요하다.
            dos = new DataOutputStream(conn.getOutputStream());

            //파일데이터(이미지파일)을 넣는 부분
            dos.writeBytes(twoHyphens + boundary + lineEnd);//각 데이터의 시작부분을 알려주는 코드
            //아래 줄은 name= 뒤에 fieldname 넣어주고, filename= 뒤에 이미지 파일명을 넣어준다.
            dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + info.getImage() + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = is.available();// 현재 읽을 수 있는 바이트 수를 반환함
            Log.i("Huzza", "Initial .available : " + bytesAvailable);

            bufferSize = Math.min(bytesAvailable, maxBufferSize);//둘중에 작은 값을 bufferSize로 넣어줌
            buffer = new byte[bufferSize];//byte타입 배열 생성

            //bufferSize만큼 읽어서 buffer의 off위치에 저장하고 읽은 바이트 수를 반환한다.
            bytesRead = is.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {//is의 끝까지 다 읽는다.
                dos.write(buffer, 0, bufferSize);// 배열 buffer의 0 위치에서 bufferSize만큼 출력한다.
                bytesAvailable = is.available();// 현재 읽을 수 있는 바이트 수를 반환함
                bufferSize = Math.min(bytesAvailable, maxBufferSize);//둘중에 작은 값을 bufferSize로 넣어줌
                bytesRead = is.read(buffer, 0, bufferSize);//bufferSize만큼 읽어서 buffer의 off위치에 저장하고 읽은 바이트 수를 반환한다.
            }

            dos.writeBytes(lineEnd);

            //보낼 데이터 중에서 이미지 이외에 나머지 데이터들 넣는부분
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"email\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getEmail());
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"pwd\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getPwd());
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"name\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getName());
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"height\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(info.getHeight()));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"weight\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(info.getWeight()));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"gender\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(info.getGender()));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"birth\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getBirth());
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"muscle\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(info.getMuscle()));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"fat\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(info.getFat()));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"intro\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getIntro());
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"trainer\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(info.getTrainer()));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"admin\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(info.getAdmin()));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"alarm\"  " +twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(info.getAlarm()));
            dos.writeBytes(lineEnd);

            //실제 서버로 request 요청하는 부분. 응답 코드 받는다. serverResponseCode가
            // HttpURLConnection.HTTP_OK(200)와 같으면 정상이고 나머지는 에러이다.
            serverResponseCode = conn.getResponseCode();

            is.close();

            dos.flush();//request body에 데이터 입력
            dos.close();//outputStream 종료.

        } catch (MalformedURLException ex) {

            ex.printStackTrace();
        } catch (FileNotFoundException e){

        }
        catch (SecurityException e){

        }
        catch (Exception e) {

            e.printStackTrace();
        }

        if (serverResponseCode == 200) {//성공한 경우


            Log.i("Huzza", "response");

            //request 시 outputStream 을 이용했지만 response 시에는 InputStream 을 이용한다.

            StringBuilder sb = new StringBuilder();
            try {
                //conn.getInputStream()는 원격 자원으로부터 데이터를 읽어온다.
                //InputStreamReader는 바이트 스트림에서 유니코드 문자 스트림으로 변환한다.
                //BufferedReader는 버퍼를 이용해서 읽도록 함으로써 읽는 효율을 높임
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                        .getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
            } catch (IOException ioex) {
            }
            return  sb.toString();//전부 읽어서 저장된 sb를 string 타입으로 봔환함.
        } else {
            return rtmsg+"Could not upload";
        }
    }
}
