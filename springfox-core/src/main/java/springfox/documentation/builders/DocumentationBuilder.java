/*
 *
 *  Copyright 2015-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package springfox.documentation.builders;

import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.ResourceListing;
import springfox.documentation.service.Tag;
import springfox.documentation.service.VendorExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static springfox.documentation.builders.BuilderDefaults.*;

public class DocumentationBuilder {
  private String groupName;
  private Map<String, List<ApiListing>> apiListings = new TreeMap<>(Comparator.naturalOrder());
  private ResourceListing resourceListing;
  private Set<Tag> tags = new LinkedHashSet<>();
  private String basePath;
  private Set<String> produces = new LinkedHashSet<>();
  private Set<String> consumes = new LinkedHashSet<>();
  private String host;
  private Set<String> schemes = new LinkedHashSet<>();
  private List<VendorExtension> vendorExtensions = new ArrayList<>();


  /**
   * Name of the documentation group
   *
   * @param groupName - group name
   * @return this
   */
  public DocumentationBuilder name(String groupName) {
    this.groupName = defaultIfAbsent(groupName, this.groupName);
    return this;
  }

  /**
   * Updates the map with new entries
   *
   * @param apiListings - entries to add to the existing documentation
   * @return this
   */
  public DocumentationBuilder apiListingsByResourceGroupName(Map<String, List<ApiListing>> apiListings) {
    nullToEmptyMultimap(apiListings).entrySet().stream().forEachOrdered(entry -> {
      List<ApiListing> list;
      if (this.apiListings.containsKey(entry.getKey())) {
        list = this.apiListings.get(entry.getKey());
        list.addAll(entry.getValue());
      } else {
        list = new ArrayList<>(entry.getValue());
        this.apiListings.put(entry.getKey(), list);
      }
      list.sort(byListingPosition());
    });
    return this;
  }

  /**
   * Updates the resource listing
   *
   * @param resourceListing - resource listing
   * @return this
   */
  public DocumentationBuilder resourceListing(ResourceListing resourceListing) {
    this.resourceListing = defaultIfAbsent(resourceListing, this.resourceListing);
    return this;
  }

  /**
   * Updates the tags with new entries
   *
   * @param tags - new tags
   * @return this
   */
  public DocumentationBuilder tags(Set<Tag> tags) {
    this.tags.addAll(nullToEmptySet(tags));
    return this;
  }

  /**
   * Updates the existing media types with new entries that this documentation produces
   *
   * @param mediaTypes - new media types
   * @return this
   */
  public DocumentationBuilder produces(Set<String> mediaTypes) {
    this.produces.addAll(nullToEmptySet(mediaTypes));
    return this;
  }

  /**
   * Updates the existing media types with new entries that this documentation consumes
   *
   * @param mediaTypes - new media types
   * @return this
   */
  public DocumentationBuilder consumes(Set<String> mediaTypes) {
    this.consumes.addAll(nullToEmptySet(mediaTypes));
    return this;
  }

  /**
   * Updates the host (name or ip) serving this api.
   *
   * @param host - new host
   * @return this
   */
  public DocumentationBuilder host(String host) {
    this.host = defaultIfAbsent(host, this.host);
    return this;
  }

  /**
   * Updates the schemes this api supports
   *
   * @param schemes - new schemes
   * @return this
   */
  public DocumentationBuilder schemes(Set<String> schemes) {
    this.schemes.addAll(nullToEmptySet(schemes));
    return this;
  }

  /**
   * Base path for this API
   *
   * @param basePath - base path
   * @return this
   */
  public DocumentationBuilder basePath(String basePath) {
    this.basePath = defaultIfAbsent(basePath, this.basePath);
    return this;
  }

  /**
   * Adds extensions for this API
   *
   * @param extensions - extensions
   * @return this
   */
  public DocumentationBuilder extensions(List<VendorExtension> extensions) {
    this.vendorExtensions.addAll(nullToEmptyList(extensions));
    return this;
  }


  public static Comparator<ApiListing> byListingPosition() {
    return Comparator.comparingInt(ApiListing::getPosition);
  }

  public Documentation build() {
    return new Documentation(
        groupName,
        basePath,
        tags,
        apiListings,
        resourceListing,
        produces,
        consumes,
        host,
        schemes,
        vendorExtensions);
  }
}
