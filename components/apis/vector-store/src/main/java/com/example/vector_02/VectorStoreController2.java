package com.example.vector_02;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.data.DataFiles;



@RestController
@RequestMapping("/vector/02")
public class VectorStoreController2 {
  
  private final Logger logger = LoggerFactory.getLogger(VectorStoreController2.class);

  private final DataFiles dataFiles;
  private final VectorStore vectorStore;
  private final EmbeddingModel embeddingModel;

  // public VectorStoreController2(VectorStore vectorStore, DataFiles dataFiles) throws IOException
  // {
  //   this.dataFiles = dataFiles;
  //   this.vectorStore = vectorStore;
  // }

  public VectorStoreController2(EmbeddingModel embeddingModel, VectorStore vectorStore, DataFiles dataFiles)
      throws IOException {
    this.embeddingModel = embeddingModel;
    this.dataFiles = dataFiles;
    this.vectorStore = vectorStore;
  }

  @GetMapping("/load")
  public String load() throws IOException {

    // Turn the .txt description file, in a document per saint.

    String msg = null;

    Resource[] saintsResources = this.dataFiles.getSaintsResource();

    if ((saintsResources != null) && (saintsResources.length > 0)) {

      for (Resource resource : saintsResources) {
        DocumentReader reader = new TextReader(resource);
        List<Document> documents = reader.get();

        // add the documents to the vector store
        this.vectorStore.add(documents);

        msg = "vector store loaded with %s documents".formatted(documents.size());

        logger.info(msg);
      }
      
    }

    msg = String.format(msg, "Total Documents Loaded: %s", saintsResources.length);
    logger.info(msg);

    return (msg);

  }

  @GetMapping("query")
  public List<String> query(
      @RequestParam(value = "topic", defaultValue = "Which Saint is known for Leadership?")
          String topic) {

    // search the vector store for the top 4 Saints that match the query
    List<Document> topMatches = this.vectorStore.similaritySearch(topic);

    return topMatches.stream().map(document -> document.getContent()).toList();
  }
}
