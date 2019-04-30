package com.android.bakingapp.model.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.JsonToken;

import com.android.bakingapp.data.JsonObject;

public class Step extends JsonObject<Step> implements Parcelable {

    private Integer id;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailURL;

    public Step() {
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    private void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoUrl;
    }

    private void setVideoURL(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    private void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public Step getObject(JsonReader jsonReader) throws Exception {
        Step step = new Step();
        if (jsonReader.peek() != JsonToken.NULL) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.peek() != JsonToken.NULL ? jsonReader.nextName() : "";
                switch (name) {
                    case "id":
                        step.setId(jsonReader.nextInt());
                        break;
                    case "shortDescription":
                        step.setShortDescription(jsonReader.nextString());
                        break;
                    case "description":
                        step.setDescription(jsonReader.nextString());
                        break;
                    case "videoURL":
                        step.setVideoURL(jsonReader.nextString());
                        break;
                    case "thumbnailURL":
                        step.setThumbnailURL(jsonReader.nextString());
                        break;
                    default:
                        jsonReader.skipValue();
                        break;
                }
            }
            jsonReader.endObject();
        } else {
            jsonReader.nextNull();
        }
        return step;
    }

    private Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
