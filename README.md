# task-management-application
A task management Android application that enables planning of both personal and collaborative work. The topic is from CS-E4100 Mobile Cloud Computing 


### Backend
The backend implemented by using the [Google App Engine Flexible Environment](https://cloud.google.com/appengine/docs/flexible/). The application use the [Firebase Cloud Firestore](https://firebase.google.com/docs/firestore) to sync information across different devices and [Cloud Storage](https://firebase.google.com/docs/storage/) to store media. In addition, it use [Cloud Endpoints](https://cloud.google.com/endpoints/) to deploy the API described in the previous section. 

The backend provides an API for project management as follows:

+ Create a user, which returns a unique project ID and sets user attributes after successful user creation.
+ Update user information.
+ Query users by username, which gives an array of required users.
+ Create a project, which returns a unique project ID after successful project creation.
+ Delete a project, which deletes all the content of a project given the project ID. This action is only available to the project administrator.
+ Add members to a project, given the project ID and a list of members (it can contain one or more users).
+ Query projects by user ID, which returns a list of projects that has that user in it.
+ Create a task, given the project ID and the task attributes. It returns the task ID after creation.
+ Update a task, which updates the task status given a task ID.
+ Assign a task to a user(s), given the project ID and the task ID.
+ Query tasks by project ID

### Run backend locally
<pre><code>npm install</code></pre>
<pre><code>npm start</code></pre>

### Backend Deployment
<p>After setting up a Google Cloud Platform (GCP) SDK in local environment</p>
<p>Deploy the Endpoints configuration to create an Endpoints service</p>
<pre><code>gcloud endpoints services deploy openapi-appengine.yaml</code></pre>
<p>Deploy the API to App Engine</p>
<pre><code>gcloud app deploy</code></pre>
