package Data;

public class SetterGetterUploads {
    String uploader,topic,date,objectId;

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUploader() {
        return uploader;
    }

    public String getDate() {
        return date;
    }

    public String getTopic() {
        return topic;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
