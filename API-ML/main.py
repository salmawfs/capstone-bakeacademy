from flask import Flask, request, jsonify
from tensorflow import keras
from io import BytesIO
import numpy as np
import os
from tensorflow.keras.preprocessing.image import load_img, img_to_array
from google.cloud import storage

app = Flask(__name__)

# model_path = os.path.join(os.path.dirname(__file__), 'model/my_model.h5')
# model = keras.models.load_model(model_path)
# def download_model_from_bucket(bucket_name, source_blob_name, destination_file_name):
#     storage_client = storage.Client()
#     bucket = storage_client.bucket(bucket_name)
#     blob = bucket.blob(source_blob_name)
#     blob.download_to_filename(destination_file_name)

# bucket_name = 'bakeacademy-bucket'
# source_blob_name = 'models/my_model.h5'
# destination_file_name = 'models/my_model.h5'

# download_model_from_bucket(bucket_name, source_blob_name, destination_file_name)

model_path = os.path.join(os.path.dirname(__file__), 'model/my_model.h5')
model = keras.models.load_model(model_path)

@app.route('/', methods=['GET'])
def index():
    return 'Model API'

@app.route('/predict', methods=['POST'])
def predict():
    file = request.files['image']
   # predicting images
    path = BytesIO(file.read())
    img = load_img(path, target_size=(224, 224))

    x = img_to_array(img)
    x = x / 255.0
    x = np.expand_dims(x, axis=0)
    images = np.vstack([x])

    classes = model.predict(images, batch_size=20)
    predicted_class_index = np.argmax(classes)
    predicted_class = class_labels[predicted_class_index]

    predict_labels = classes[0].tolist()
    print(predict_labels)

    predicted_number = predict_labels[predicted_class_index]
    # print(predicted_class)

    return jsonify({
        "predicted_class": predicted_class,
        "accuracy": predicted_number
    })
class_labels = {
    0: 'Bagel',
    1: 'Baguette',
    2: 'Biscuits',
    3: 'Croissant',
    4: 'English Muffin',
    5: 'Focaccia',
    6: 'Mantou',
    7: 'Naan',
    8: 'Pretzel',
    9: 'Random',
    10: 'Sourdough'
    # Add more class indices and labels as needed
}

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, debug=True)