package com.example.data;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

@Service
public class DataFiles {

  @Value("classpath:/data/books/Shakespeare.txt")
  private Resource shakespeareWorksResource;

  @Value("classpath:/data/bikes/bikes.json")
  private Resource bikesResource;

  @Value("classpath:/data/pdf/bylaw.pdf")
  private Resource bylawResource;

  private Resource[] saintsResource;

  public Resource getBylawResource() {
    return bylawResource;
  }

  public Resource getBikesResource() {
    return bikesResource;
  }

  public Resource getShakespeareWorksResource() {
    return shakespeareWorksResource;
  }

  public Resource[] getSaintsResource() throws IOException {
    if (saintsResource == null) {
      saintsResource = loadSaintResources();
    }

    return (saintsResource);
    
  }

  public DataFiles() throws IOException {
    saintsResource = getSaintsResource();
  }

  private Resource[] loadSaintResources() throws IOException {
    ClassLoader classLoader = MethodHandles.lookup().getClass().getClassLoader();
    PathMatchingResourcePatternResolver resolver =
        new PathMatchingResourcePatternResolver(classLoader);

    return resolver.getResources("classpath:data/saints/*.txt");
  }
}
