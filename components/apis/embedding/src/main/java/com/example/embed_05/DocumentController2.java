package com.example.embed_05;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.data.DataFiles;


@RestController
@RequestMapping("/embed/05")
public class DocumentController2 {

  private final Logger logger = LoggerFactory.getLogger(DocumentController2.class);

  private final EmbeddingModel embeddingModel;
  private final DataFiles dataFiles;

  public DocumentController2(EmbeddingModel embeddingModel, DataFiles dataFiles)
      throws IOException {
    this.embeddingModel = embeddingModel;
    this.dataFiles = dataFiles;
  }

  @GetMapping("text/saints")
  public String getSaints() throws IOException {

    String returnValue = null;
    Resource[] saintsResources = this.dataFiles.getSaintsResource();

    for (Resource resource : saintsResources) {
      DocumentReader reader = new TextReader(resource);

      List<Document> documents = reader.get();
      TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
      List<Document> chunks = tokenTextSplitter.apply(documents);

      Document document = chunks.get(0);
      float[] embedding = this.embeddingModel.embed(document);

      returnValue = """
          Input file was parsed into %s documents
          The document was split into %s chunks
          Embedding for example document computed has %s dimensions
          document id is %s
          document metadata is %s
          document embedding is %s
          """
          .formatted(
              documents.size(),
              chunks.size(),
              Integer.valueOf(embedding.length),
              document.getId(),
              document.getMetadata(),
              document.getEmbedding());
              //document.getContent());

      logger.info(returnValue);

    }

    return (returnValue);

  }


    // List<Document> documents = reader.get();
    // TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
    // List<Document> chunks = tokenTextSplitter.apply(documents);

    // Document document = chunks.get(0);
    // float[] embedding = this.embeddingModel.embed(document);

    // returnValue =
    // """
    //             Input file was parsed into %s documents
    //             The document was too big and it was split into %s chunks
    //             Embedding for example document computed has %s dimensions
    //             document id is %s
    //             document metadata is %s
    //             document embedding is %s
    //             Example contents after the dashed line below
    //             ---
    //             %s
    //             """
    //     .formatted(
    //         documents.size(),
    //         chunks.size(),
    //         Integer.valueOf(embedding.length),
    //         document.getId(),
    //         document.getMetadata(),
    //         document.getEmbedding(),
    //         document.getContent());

    // return """
    //             Input file was parsed into %s documents
    //             The document was too big and it was split into %s chunks
    //             Embedding for example document computed has %s dimensions
    //             document id is %s
    //             document metadata is %s
    //             document embedding is %s
    //             Example contents after the dashed line below
    //             ---
    //             %s
    //             """
    //     .formatted(
    //         documents.size(),
    //         chunks.size(),
    //         Integer.valueOf(embedding.length),
    //         document.getId(),
    //         document.getMetadata(),
    //         document.getEmbedding(),
    //         document.getContent());


  

  // @GetMapping("pdf/pages")
  // public String getBylaw() {
  //   PagePdfDocumentReader pdfReader =
  //       new PagePdfDocumentReader(
  //           this.dataFiles.getBylawResource(),
  //           PdfDocumentReaderConfig.builder()
  //               .withPageExtractedTextFormatter(
  //                   ExtractedTextFormatter.builder()
  //                       .withNumberOfBottomTextLinesToDelete(3)
  //                       .withNumberOfTopPagesToSkipBeforeDelete(1)
  //                       .build())
  //               .withPagesPerDocument(1)
  //               .build());
  //   List<Document> documents = pdfReader.get();

  //   var pdfToDocsSummary =
  //       """
  //           Input pdf read from %s
  //           Each page of the pdf was turned into a Document object
  //           %s total Document objects were created
  //           document id is %s
  //           document metadata is %s
  //           first document contents between the two dashed lines below
  //           ---
  //           %s
  //           ---
  //           """
  //           .formatted(
  //               dataFiles.getBylawResource().getFilename(),
  //               documents.get(0).getId(),
  //               documents.get(0).getMetadata(),
  //               documents.size(),
  //               documents.get(0).getContent());

  //   TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
  //   List<Document> chunks = tokenTextSplitter.apply(documents);
  //   Document document = documents.get(0);
  //   float[] embedding = this.embeddingModel.embed(document);

  //   var chuckSummary =
  //       """
  //               \nPDF page per doc might be too big we split each doc into chunks
  //               %s chunk documents were created
  //               Embedding for example first chunk computed has %s dimensions
  //               document id is %s
  //               document metadata is %s
  //               document embedding is %s
  //               Example contents after the dashed line below
  //               ---
  //               %s
  //               """
  //           .formatted(
  //               chunks.size(),
  //               Integer.valueOf(embedding.length),
  //               document.getId(),
  //               document.getMetadata(),
  //               document.getEmbedding(),
  //               document.getContent());

  //   return pdfToDocsSummary + chuckSummary;
  // }

  // @GetMapping("/pdf/para")
  // public String paragraphs() {
  //   ParagraphPdfDocumentReader pdfReader =
  //       new ParagraphPdfDocumentReader(
  //           this.dataFiles.getBylawResource(), PdfDocumentReaderConfig.builder().build());

  //   List<Document> documents = pdfReader.get();

  //   this.embeddingModel.embed(documents.get(0));
  //   var pdfToDocsSummary =
  //       """
  //           Input pdf read from %s
  //           Each paragraph of the pdf was turned into a Document object
  //           %s total Document objects were created
  //           document id is %s
  //           document metadata is %s
  //           first document contents between the two dashed lines below
  //           ---
  //           %s
  //           ---
  //           """
  //           .formatted(
  //               this.dataFiles.getBylawResource().getFilename(),
  //               documents.get(0).getId(),
  //               documents.get(0).getMetadata(),
  //               documents.size(),
  //               documents.get(0).getContent());

  //   return pdfToDocsSummary;
  // }
}
