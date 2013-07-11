AndroidPDFViewerLibrary
=======================

This library converts pdf files into images in android.

In the example below i will show you how to use it.

After you have imported the library into your project you need to create your activity.

The XML:

    <?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
		android:layout_width="match_parent"
		android:layout_height="match_parent">
	
		<WebView
				android:id="@+id/webView1"
				android:layout_width="match_parent"
				android:layout_height="match_parent"/>
	
	</LinearLayout>

The java onCreate:

	WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Settings
		PDFImage.sShowImages = true; // show images
		PDFPaint.s_doAntiAlias = true; // make text smooth
		HardReference.sKeepCaches = true; // save images in cache
		
		//Setup webview
		wv = (WebView)findViewById(R.id.webView1);
		wv.getSettings().setBuiltInZoomControls(true);//show zoom buttons
		wv.getSettings().setSupportZoom(true);//allow zoom
		pdfLoadImages();//load images
	}

Load Images:

	private void pdfLoadImages()
	{
		try
		{
			// run async
			new AsyncTask<Void, Void, Void>()
					{
						// create and show a progress dialog
						ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
						
						@Override
						protected void onPostExecute(Void result)
						{
							//after async close progress dialog
							progressDialog.dismiss();
						}
						
						@Override
						protected Void doInBackground(Void... params)
						{
							try
							{
								// select a document and get bytes
								File file = new File(Environment.getExternalStorageDirectory().getPath()+"/randompdf.pdf");
						        RandomAccessFile raf = new RandomAccessFile(file, "r");
						        FileChannel channel = raf.getChannel();
						        ByteBuffer bb = ByteBuffer.NEW(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
						        raf.close();
								// create a pdf doc
						        PDFFile pdf = new PDFFile(bb);
								// create arrays to hold pages
						        Bitmap[] pages = new Bitmap[pdf.getNumPages()];
						        PDFPage[] PDFpages = new PDFPage[pdf.getNumPages()];
								
						        OutputStream stream;
								// create html for the webview
						        String html = "<!DOCTYPE html><html><body bgcolor=\"#7f7f7f\">";
								// create scale value.
						        float scale;
						        
								// run through each page
								for(int i = 0; i < pdf.getNumPages(); i++)
						        {
							        // get page from pdf
									PDFpages[i] = pdf.getPage(i, true);
							        //save page to sd card
									stream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/"+i+".png");
							        //add page to array
						            pages[i] = PDFpages[i].getImage((int)PDFpages[i].getWidth(), (int)PDFpages[i].getHeight(), null, true, true);
						            pages[i].compress(CompressFormat.PNG, 100, stream);
							        //append html
						            html += "<img src=\"file:///"+Environment.getExternalStorageDirectory().getPath()+"/"+i+".png\" hspace=10 vspace=10><br>";
						        }
								//close html tags
								html += "</body></html>";
								//load html to webview
								wv.loadDataWithBaseURL("", html, "text/html","UTF-8", "");
							}
							catch (Exception e)
							{
								Log.d("error", e.toString());
							}
							return null;
						}
					}.execute();
					System.gc();// run GC
		}
		catch (Exception e)
		{
			Log.d("error", e.toString());
		}
	}
