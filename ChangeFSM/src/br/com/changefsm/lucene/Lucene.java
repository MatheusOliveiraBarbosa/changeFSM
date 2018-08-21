package br.com.changefsm.lucene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.QueryBuilder;

import br.com.changefsm.models.ClassChanged;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Lucene implements IFLucene {

	private IndexWriter indexWriter;
	private final String CLAZZ = "class";
	private final String imports = "import";
	private final String pathClass = "path";
	private Directory ramDir;
	private StandardAnalyzer analyzer;
	private final Logger log = LogManager.getLogger(Lucene.class);

	/**
	 * Initialize the indexer of the Lucene.
	 */
	@Override
	public void createInderWriter() throws IOException {
		this.analyzer = new StandardAnalyzer();
		this.ramDir = new RAMDirectory();
		IndexWriterConfig iwConf = new IndexWriterConfig(analyzer);
		indexWriter = new IndexWriter(this.ramDir, iwConf);
	}

	/**
	 * Index the list of class in the Lucene
	 * 
	 * @param classesChanged
	 * @throws IOException
	 */
	@Override
	public void indexerClass(List<ClassChanged> classesChanged) throws IOException {
		for (ClassChanged classChanged : classesChanged) {
			Document doc = new Document();
			String forIndexing = transformFileToString(classChanged.getClassFile());
			doc.add(new TextField(CLAZZ, forIndexing, Field.Store.YES));
			doc.add(new TextField(pathClass, classChanged.getClassFile().getPath(), Field.Store.YES));
			indexWriter.addDocument(doc);
		}
		indexWriter.close();
	}

	/**
	 * Transform the lines of the file in a String to can possible to index in
	 * Lucene
	 * 
	 * @param classFile
	 * @return <code> String </code> that is the whole text in file;
	 */
	private String transformFileToString(File classFile) {
		String fileToString = "";
		try {
			Scanner scanner = new Scanner(classFile);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				// Ignore the lines with imports packages/libs...
				if (!line.startsWith(imports)) {
					fileToString += line;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			log.error("Didn't find the file!");
		}
		return fileToString;
	}

	/**
	 * Search for class with terms of the State and Transition on StateMachine.
	 * 
	 * @param keyWords
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<ClassChanged> searchFilesRelated(List<String> keyWords) throws IOException {
		IndexReader indexReader = DirectoryReader.open(this.ramDir);
		IndexSearcher searcher = new IndexSearcher(indexReader);

		String queryText = convertListStringToString(keyWords);

		QueryBuilder qb = new QueryBuilder(this.analyzer);
		Query query = qb.createBooleanQuery(CLAZZ, queryText);

		TopDocs topDocs = searcher.search(query, 100);
		List<ClassChanged> classesRelated = new ArrayList<ClassChanged>();

		// To consider that a class is related, the class should has score >60% than the
		// biggest score
		double porcentToSelect = topDocs.getMaxScore() * (0.6);

		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			if (porcentToSelect < scoreDoc.score) {
				log.info("Score by Lucene in the class " + searcher.doc(scoreDoc.doc).getField(pathClass).stringValue()
						+ " is: " + scoreDoc.score);
				ClassChanged classRelated = new ClassChanged();
				File file = new File(searcher.doc(scoreDoc.doc).getField(pathClass).stringValue());
				classRelated.setClassFile(file);
				classesRelated.add(classRelated);
			}
		}

		return classesRelated;
	}

	/**
	 * Convert the List of Strings to one String addicting the expression OR to
	 * improve the search.
	 * 
	 * @param keyWords
	 * @return String with all elements separate by conditional "OR"
	 */
	private String convertListStringToString(List<String> keyWords) {
		String queryText = "";

		if (keyWords.size() > 1) {
			for (int i = 0; i < keyWords.size(); i++) {
				// Verify weather the last iterate for don't put conditional "OR"
				if ((i + 1) == keyWords.size()) {
					queryText += keyWords.get(i);
				} else {
					queryText += keyWords.get(i) + " OR ";
				}
			}
		}
		// Weather list has only String the query is a simple word
		else if (keyWords.size() == 1) {
			return keyWords.get(0);
		}
		return queryText;
	}
}
